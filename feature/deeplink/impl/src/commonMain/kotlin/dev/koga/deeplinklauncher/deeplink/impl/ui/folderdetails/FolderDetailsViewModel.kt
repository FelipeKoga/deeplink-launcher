@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.impl.analytics.DeeplinkLaunchFailed
import dev.koga.deeplinklauncher.deeplink.impl.analytics.DeeplinkLaunched
import dev.koga.deeplinklauncher.deeplink.impl.analytics.FolderDeleted
import dev.koga.deeplinklauncher.deeplink.impl.analytics.track
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.state.FolderDetailsAction
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.state.FolderDetailsUiState
import dev.koga.deeplinklauncher.deeplink.ui.EnrichDeepLinksForList
import dev.koga.deeplinklauncher.deeplink.ui.model.DeepLinkListItem
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.model.LaunchSource
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.navigation.AppNavigator
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class FolderDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FolderRepository,
    private val enrichDeepLinksForList: EnrichDeepLinksForList,
    private val launchDeepLink: LaunchDeepLink,
    private val appNavigator: AppNavigator,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel() {
    private val folderId = savedStateHandle.toRoute<DeepLinkRouteEntryPoint.FolderDetails>().id

    private val folder = repository.getFolderByIdStream(folderId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    private val form = MutableStateFlow(
        FolderDetailsUiState(
            name = "",
            description = "",
            deepLinks = persistentListOf(),
        ),
    )

    private val deepLinksState = repository.getFolderDeepLinksStream(folderId)
        .flatMapLatest { links ->
            flow {
                emit(
                    DeepLinksState(
                        deepLinks = enrichDeepLinksForList(links),
                        isLoaded = true,
                    ),
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = DeepLinksState(),
        )

    val uiState = combine(form, deepLinksState, folder) { form, deepLinksState, folder ->
        form.copy(
            deepLinks = deepLinksState.deepLinks.toPersistentList(),
            isDeepLinksLoaded = deepLinksState.isLoaded,
            isFolderLoaded = folder != null,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = form.value,
    )

    init {
        viewModelScope.launch {
            when (val loadedFolder = repository.getFolderByIdStream(folderId).first()) {
                null -> appNavigator.popBackStack()
                else -> {
                    form.update {
                        it.copy(
                            name = loadedFolder.name,
                            description = loadedFolder.description.orEmpty(),
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: FolderDetailsAction) {
        when (action) {
            FolderDetailsAction.Delete -> delete()
            is FolderDetailsAction.Launch -> launch(action.deeplink)
            is FolderDetailsAction.UpdateDescription -> updateDescription(action.text)
            is FolderDetailsAction.UpdateName -> updateName(action.text)
            FolderDetailsAction.OpenLinkDeepLinkScreen -> openLinkDeepLinkScreen()
        }
    }

    private fun delete() {
        viewModelScope.launch {
            repository.delete(folderId)
            analyticsTracker.track(FolderDeleted)
            appNavigator.popBackStack()
        }
    }

    // The form holds the saved values: each edit is written first and shown only if it was
    // accepted, so a rejected name never hides an unsaved description or vice versa.
    private fun updateName(value: String) {
        viewModelScope.launch {
            val saved = form.value
            when (repository.update(id = folderId, name = value, description = saved.description.ifBlank { null })) {
                FolderRepository.WriteResult.Success -> form.update { it.copy(name = value, nameError = null) }
                FolderRepository.WriteResult.NameAlreadyExists ->
                    form.update { it.copy(nameError = "A folder named \"$value\" already exists") }

                FolderRepository.WriteResult.NotFound -> appNavigator.popBackStack()
            }
        }
    }

    private fun updateDescription(value: String) {
        viewModelScope.launch {
            val saved = form.value
            when (repository.update(id = folderId, name = saved.name, description = value.ifBlank { null })) {
                FolderRepository.WriteResult.Success -> form.update { it.copy(description = value) }
                FolderRepository.WriteResult.NameAlreadyExists -> Unit
                FolderRepository.WriteResult.NotFound -> appNavigator.popBackStack()
            }
        }
    }

    private fun launch(deepLink: DeepLink) {
        viewModelScope.launch {
            when (launchDeepLink.launch(deepLink)) {
                is LaunchDeepLink.Result.Success -> {
                    analyticsTracker.track(
                        DeeplinkLaunched(source = LaunchSource.FOLDER),
                    )
                }

                is LaunchDeepLink.Result.Failure -> {
                    analyticsTracker.track(
                        DeeplinkLaunchFailed(source = LaunchSource.FOLDER),
                    )
                }
            }
        }
    }

    private fun openLinkDeepLinkScreen() {
        appNavigator.navigate(DeepLinkRouteEntryPoint.PickDeepLinkForFolder(folderId))
    }

    private data class DeepLinksState(
        val deepLinks: List<DeepLinkListItem> = emptyList(),
        val isLoaded: Boolean = false,
    )
}
