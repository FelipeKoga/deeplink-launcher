package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.navigation.AppNavigator
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class FolderDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: FolderRepository,
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

    private val deepLinks = repository.getFolderDeepLinksStream(folderId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    val uiState = combine(
        form,
        deepLinks,
    ) { form, deepLinks ->
        form.copy(
            deepLinks = deepLinks.toPersistentList(),
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
}
