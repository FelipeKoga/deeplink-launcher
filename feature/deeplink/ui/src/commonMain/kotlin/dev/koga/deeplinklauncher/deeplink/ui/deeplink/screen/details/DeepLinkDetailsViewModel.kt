@file:OptIn(ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.coroutines.CoroutineDebouncer
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.DuplicateDeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.ShareDeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.ValidateDeepLink
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.event.DeepLinkDetailsEvent
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.navigation.AppNavigator
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class DeepLinkDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val folderRepository: FolderRepository,
    private val deepLinkRepository: DeepLinkRepository,
    private val launchDeepLink: LaunchDeepLink,
    private val shareDeepLink: ShareDeepLink,
    private val duplicateDeepLink: DuplicateDeepLink,
    private val validateDeepLink: ValidateDeepLink,
    private val coroutineDebouncer: CoroutineDebouncer,
    private val appNavigator: AppNavigator,
) : ViewModel(), AppNavigator by appNavigator {
    private val id = savedStateHandle.get<String>("deeplink_id").orEmpty()

    private val deepLink = deepLinkRepository.getDeepLinkByIdStream(id)
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
                errorMessage = deepLinkErrorMessage
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

    private val eventDispatcher = Channel<DeepLinkDetailsEvent>(Channel.UNLIMITED)
    val events = eventDispatcher.receiveAsFlow()

    fun onAction(action: Action) {
        when (action) {
            is EditAction -> onEditAction(action)
            is LaunchAction -> onLaunchAction(action)
            is DuplicateAction -> onDuplicateAction(action)
        }
    }

    private fun onLaunchAction(action: LaunchAction) {
        when (action) {
            LaunchAction.Delete -> delete()
            LaunchAction.Duplicate -> mode.update { Mode.DUPLICATE }
            LaunchAction.Edit -> mode.update { Mode.EDIT }
            LaunchAction.Launch -> launch()
            LaunchAction.Share -> share()
            LaunchAction.ToggleFavorite -> toggleFavorite()
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
            is EditAction.AddFolder -> insertFolder(action.name, action.description)
            is EditAction.OnDescriptionChanged -> updateDescription(action.text)
            is EditAction.OnLinkChanged -> updateLink(action.text)
            is EditAction.OnNameChanged -> updateName(action.text)
            is EditAction.ToggleFolder -> toggleFolder(action.folder)
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
            eventDispatcher.send(DeepLinkDetailsEvent.Deleted)
        }
    }

    private fun share() {
        shareDeepLink(deepLink.value)
    }

    private fun insertFolder(name: String, description: String) {
        val folder = Folder(
            id = Uuid.random().toString(),
            name = name,
            description = description,
        )

        folderRepository.upsertFolder(folder)

        toggleFolder(folder)
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
                    eventDispatcher.send(DeepLinkDetailsEvent.Duplicated(response.deepLink))
                }
            }
        }
    }

    private enum class Mode {
        LAUNCH,
        EDIT,
        DUPLICATE
    }
}
