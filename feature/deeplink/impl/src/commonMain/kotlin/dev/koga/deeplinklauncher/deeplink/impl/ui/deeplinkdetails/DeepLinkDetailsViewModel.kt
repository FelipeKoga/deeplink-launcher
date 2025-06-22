package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.koga.deeplinklauncher.coroutines.CoroutineDebouncer
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.api.usecase.DuplicateDeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.ShareDeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.ValidateDeepLink
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsAction
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.DuplicateAction
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.EditAction
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state.LaunchAction
import dev.koga.deeplinklauncher.navigation.AppNavigator
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class DeepLinkDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    folderRepository: FolderRepository,
    private val deepLinkRepository: DeepLinkRepository,
    private val launchDeepLink: LaunchDeepLink,
    private val shareDeepLink: ShareDeepLink,
    private val duplicateDeepLink: DuplicateDeepLink,
    private val validateDeepLink: ValidateDeepLink,
    private val coroutineDebouncer: CoroutineDebouncer,
    private val appNavigator: AppNavigator,
) : ViewModel(), AppNavigator by appNavigator {

    private val route = savedStateHandle.toRoute<DeepLinkRouteEntryPoint.DeepLinkDetails>()
    private val deepLink = deepLinkRepository.getDeepLinkByIdStream(route.id)
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DeepLink.empty,
        )

    private val folders = folderRepository.getFoldersStream().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    private val duplicateErrorMessage = MutableStateFlow<String?>(null)
    private val deepLinkErrorMessage = MutableStateFlow<String?>(null)
    private val mode = MutableStateFlow(Mode.LAUNCH)

    val uiState = combine(
        folders,
        deepLink,
        duplicateErrorMessage,
        deepLinkErrorMessage,
        mode,
    ) { folders, deepLink, duplicateErrorMessage, deepLinkErrorMessage, mode ->
        when (mode) {
            Mode.LAUNCH -> DeepLinkDetailsUiState.Launch(deepLink)
            Mode.EDIT -> DeepLinkDetailsUiState.Edit(
                deepLink = deepLink,
                folders = folders.toPersistentList(),
                errorMessage = deepLinkErrorMessage,
            )

            Mode.DUPLICATE -> DeepLinkDetailsUiState.Duplicate(
                deepLink = deepLink,
                errorMessage = duplicateErrorMessage,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = DeepLinkDetailsUiState.Launch(
            deepLink = deepLink.value,
        ),
    )

    fun onAction(action: DeepLinkDetailsAction) {
        when (action) {
            is EditAction -> onEditAction(action)
            is LaunchAction -> onLaunchAction(action)
            is DuplicateAction -> onDuplicateAction(action)
        }
    }

    private fun onLaunchAction(action: LaunchAction) {
        when (action) {
            LaunchAction.Duplicate -> mode.update { Mode.DUPLICATE }
            LaunchAction.Edit -> mode.update { Mode.EDIT }
            LaunchAction.Launch -> launch()
            LaunchAction.Share -> share()
            LaunchAction.ToggleFavorite -> toggleFavorite()
            LaunchAction.NavigateToFolder -> appNavigator.navigate(
                DeepLinkRouteEntryPoint.FolderDetails(
                    id = deepLink.value.folder?.id.orEmpty(),
                ),
            )
        }
    }

    private fun onDuplicateAction(action: DuplicateAction) {
        when (action) {
            is DuplicateAction.Duplicate -> duplicate(action.newLink, action.copyAllFields)
            DuplicateAction.Back -> mode.update { Mode.LAUNCH }
        }
    }

    private fun onEditAction(action: EditAction) {
        when (action) {
            is EditAction.AddFolder -> appNavigator.navigate(DeepLinkRouteEntryPoint.AddFolder)
            is EditAction.OnDescriptionChanged -> updateDescription(action.text)
            is EditAction.OnLinkChanged -> updateLink(action.text)
            is EditAction.OnNameChanged -> updateName(action.text)
            is EditAction.ToggleFolder -> toggleFolder(action.folder)
            EditAction.Delete -> delete()
            EditAction.Back -> mode.update { Mode.LAUNCH }
        }
    }

    private fun updateLink(link: String) {
        deepLinkErrorMessage.update { null }

        coroutineDebouncer.debounce(viewModelScope, "link") {
            deepLinkRepository.upsertDeepLink(deepLink.value.copy(link = link))

            if (!validateDeepLink.isValid(link)) {
                deepLinkErrorMessage.update { "Invalid deeplink" }
            }
        }
    }

    private fun updateName(name: String) {
        coroutineDebouncer.debounce(viewModelScope, "name") {
            deepLinkRepository.upsertDeepLink(deepLink.value.copy(name = name))
        }
    }

    private fun updateDescription(description: String) {
        coroutineDebouncer.debounce(viewModelScope, "description") {
            deepLinkRepository.upsertDeepLink(deepLink.value.copy(description = description))
        }
    }

    private fun toggleFavorite() {
        deepLinkRepository.upsertDeepLink(
            deepLink.value.copy(isFavorite = !deepLink.value.isFavorite),
        )
    }

    private fun launch() {
        viewModelScope.launch {
            launchDeepLink.launch(deepLink.value)
        }
    }

    private fun delete() {
        viewModelScope.launch {
            deepLinkRepository.deleteDeepLink(deepLink.value.id)
            appNavigator.popBackStack()
        }
    }

    private fun share() {
        shareDeepLink(deepLink.value)
    }

    private fun toggleFolder(folder: Folder) {
        deepLinkRepository.upsertDeepLink(
            deepLink.value.copy(folder = folder.takeIf { folder.id != deepLink.value.folder?.id }),
        )
    }

    private fun duplicate(
        newLink: String,
        copyAllFields: Boolean,
    ) {
        duplicateErrorMessage.update { null }

        viewModelScope.launch {
            val response = duplicateDeepLink(
                deepLinkId = deepLink.value.id,
                newLink = newLink,
                copyAllFields = copyAllFields,
            )

            when (response) {
                DuplicateDeepLink.Result.Error.InvalidLink -> {
                    duplicateErrorMessage.update {
                        "No app found to handle this deep link: $newLink"
                    }
                }

                DuplicateDeepLink.Result.Error.LinkAlreadyExists -> {
                    duplicateErrorMessage.update { "Link already exists" }
                }

                DuplicateDeepLink.Result.Error.SameLink -> {
                    duplicateErrorMessage.update { "Link is the same as the original one" }
                }

                is DuplicateDeepLink.Result.Success -> {
                    appNavigator.popBackStack()
                    appNavigator.navigate(
                        route = DeepLinkRouteEntryPoint.DeepLinkDetails(
                            id = response.deepLink.id,
                            showFolder = true,
                        ),
                    )
                }
            }
        }
    }

    private enum class Mode {
        LAUNCH,
        EDIT,
        DUPLICATE,
    }
}
