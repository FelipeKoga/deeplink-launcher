@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.application.EnrichDeepLinksForList
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.state.FolderDetailsAction
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.state.FolderDetailsUiState
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkInputState
import dev.koga.deeplinklauncher.navigation.AppNavigator
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class FolderDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FolderRepository,
    private val deepLinkRepository: DeepLinkRepository,
    private val enrichDeepLinksForList: EnrichDeepLinksForList,
    private val getAutoSuggestionLinks: GetAutoSuggestionLinks,
    private val launchDeepLink: LaunchDeepLink,
    private val appNavigator: AppNavigator,
) : ViewModel() {
    private val folderId = savedStateHandle.toRoute<DeepLinkRouteEntryPoint.FolderDetails>().id

    private val folder = repository.getFolderById(folderId)!!

    private val form = MutableStateFlow(
        folder.let {
            FolderDetailsUiState(
                name = it.name,
                description = it.description.orEmpty(),
                deepLinks = persistentListOf(),
            )
        },
    )

    private val launchInput = MutableStateFlow("")
    private val errorMessage = MutableStateFlow<String?>(null)
    private val suggestions = launchInput.mapLatest { getAutoSuggestionLinks(it) }

    private val deepLinkInputState =
        combine(launchInput, errorMessage, suggestions, ::DeepLinkInputState)

    private val deepLinks = repository.getFolderDeepLinksStream(folderId)
        .flatMapLatest { links ->
            flow {
                emit(enrichDeepLinksForList(links))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )

    val uiState = combine(
        form,
        deepLinks,
        deepLinkInputState,
    ) { form, deepLinks, deepLinkInputState ->
        form.copy(
            deepLinks = deepLinks.toPersistentList(),
            deepLinkInputState = deepLinkInputState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = form.value,
    )

    init {
        form.onEach {
            repository.upsertFolder(
                folder.copy(
                    name = it.name,
                    description = it.description,
                ),
            )
        }.launchIn(viewModelScope)
    }

    fun onAction(action: FolderDetailsAction) {
        when (action) {
            FolderDetailsAction.Delete -> delete()
            is FolderDetailsAction.Launch -> launch(action.deeplink)
            is FolderDetailsAction.UpdateDescription -> updateDescription(action.text)
            is FolderDetailsAction.UpdateName -> updateName(action.text)
            FolderDetailsAction.LaunchInputDeepLink -> launchInputDeepLink()
            is FolderDetailsAction.OnInputChanged -> onDeepLinkTextChanged(action.text)
            is FolderDetailsAction.OnSuggestionClicked -> onDeepLinkTextChanged(action.suggestion.text)
            FolderDetailsAction.ConfirmLinkToFolder -> confirmLinkToFolder()
            FolderDetailsAction.DismissLinkConfirmation -> dismissLinkConfirmation()
        }
    }

    private fun delete() {
        repository.deleteFolder(folderId)
        appNavigator.popBackStack()
    }

    private fun updateName(value: String) {
        form.update { it.copy(name = value) }
    }

    private fun updateDescription(value: String) {
        form.update { it.copy(description = value) }
    }

    private fun launch(deepLink: DeepLink) {
        viewModelScope.launch {
            launchDeepLink.launch(deepLink)
        }
    }

    private fun launchInputDeepLink() = viewModelScope.launch {
        val link = uiState.value.deepLinkInputState.text
        val existing = deepLinkRepository.getDeepLinkByLink(link)

        if (existing != null) {
            when (launchDeepLink.launch(existing)) {
                is LaunchDeepLink.Result.Success -> {
                    if (existing.folder?.id != folderId) {
                        form.update { it.copy(pendingLinkConfirmation = existing) }
                    }
                }

                is LaunchDeepLink.Result.Failure -> showLaunchError(link)
            }
            return@launch
        }

        when (launchDeepLink.launch(link)) {
            is LaunchDeepLink.Result.Success -> insertDeepLinkWithFolder(link)
            is LaunchDeepLink.Result.Failure -> showLaunchError(link)
        }
    }

    private fun insertDeepLinkWithFolder(link: String) {
        deepLinkRepository.upsertDeepLink(
            DeepLink(
                id = Uuid.random().toString(),
                link = link,
                name = null,
                description = null,
                folder = folder,
                isFavorite = false,
                lastLaunchedAt = currentLocalDateTime,
            ),
        )
    }

    private fun confirmLinkToFolder() {
        val pendingDeepLink = uiState.value.pendingLinkConfirmation ?: return

        deepLinkRepository.upsertDeepLink(
            pendingDeepLink.copy(folder = folder),
        )
        form.update { it.copy(pendingLinkConfirmation = null) }
    }

    private fun dismissLinkConfirmation() {
        form.update { it.copy(pendingLinkConfirmation = null) }
    }

    private fun onDeepLinkTextChanged(text: String) {
        errorMessage.update { null }
        launchInput.update { text }
    }

    private fun showLaunchError(link: String) {
        errorMessage.update {
            "No app found to handle this deep link: $link"
        }
    }
}
