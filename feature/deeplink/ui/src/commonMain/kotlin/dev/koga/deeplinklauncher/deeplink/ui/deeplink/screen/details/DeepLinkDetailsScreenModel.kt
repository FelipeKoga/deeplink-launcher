@file:OptIn(ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
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
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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

class DeepLinkDetailsScreenModel(
    deepLinkId: String,
    private val folderRepository: FolderRepository,
    private val deepLinkRepository: DeepLinkRepository,
    private val launchDeepLink: LaunchDeepLink,
    private val shareDeepLink: ShareDeepLink,
    private val duplicateDeepLink: DuplicateDeepLink,
    private val validateDeepLink: ValidateDeepLink,
) : ScreenModel {

    private val coroutineDebouncer = CoroutineDebouncer()

    private val deepLink = deepLinkRepository.getDeepLinkByIdStream(deepLinkId)
        .filterNotNull()
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DeepLink.empty,
        )

    private val folders = folderRepository.getFoldersStream().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    private val duplicateErrorMessage = MutableStateFlow<String?>(null)
    private val deepLinkErrorMessage = MutableStateFlow<String?>(null)

    val uiState = combine(
        folders,
        deepLink,
        duplicateErrorMessage,
        deepLinkErrorMessage,
    ) { folders, deepLink, duplicateErrorMessage, deepLinkErrorMessage ->
        DeepLinkDetailsUiState(
            folders = folders.toPersistentList(),
            deepLink = deepLink,
            duplicateErrorMessage = duplicateErrorMessage,
            deepLinkErrorMessage = deepLinkErrorMessage,
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = DeepLinkDetailsUiState(
            folders = persistentListOf(),
            deepLink = deepLink.value,
        ),
    )

    private val eventDispatcher = Channel<DeepLinkDetailsEvent>(Channel.UNLIMITED)
    val events = eventDispatcher.receiveAsFlow()

    fun updateLink(link: String) {
        deepLinkErrorMessage.update { null }

        coroutineDebouncer.debounce(screenModelScope, "link") {
            deepLinkRepository.upsertDeepLink(deepLink.value.copy(link = link))

            if (!validateDeepLink.isValid(link)) {
                deepLinkErrorMessage.update { "Invalid deeplink" }
            }
        }
    }

    fun updateName(name: String) {
        coroutineDebouncer.debounce(screenModelScope, "name") {
            deepLinkRepository.upsertDeepLink(deepLink.value.copy(name = name))
        }
    }

    fun updateDescription(description: String) {
        coroutineDebouncer.debounce(screenModelScope, "description") {
            deepLinkRepository.upsertDeepLink(deepLink.value.copy(description = description))
        }
    }

    fun toggleFavorite() {
        deepLinkRepository.upsertDeepLink(
            deepLink.value.copy(isFavorite = !deepLink.value.isFavorite),
        )
    }

    fun launch() {
        screenModelScope.launch {
            launchDeepLink.launch(deepLink.value)
        }
    }

    fun delete() {
        screenModelScope.launch {
            deepLinkRepository.deleteDeepLink(deepLink.value.id)
            eventDispatcher.send(DeepLinkDetailsEvent.Deleted)
        }
    }

    fun share() {
        shareDeepLink(deepLink.value)
    }

    fun insertFolder(name: String, description: String) {
        val folder = Folder(
            id = Uuid.random().toString(),
            name = name,
            description = description,
        )

        folderRepository.upsertFolder(folder)

        toggleFolder(folder)
    }

    fun toggleFolder(folder: Folder) {
        deepLinkRepository.upsertDeepLink(
            deepLink.value.copy(folder = folder.takeIf { folder.id != deepLink.value.folder?.id }),
        )
    }

    fun duplicate(
        newLink: String,
        copyAllFields: Boolean,
    ) {
        duplicateErrorMessage.update { null }

        screenModelScope.launch {
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

    private class CoroutineDebouncer {
        private val jobs = mutableMapOf<String, Job>()

        fun debounce(
            coroutineScope: CoroutineScope,
            key: String,
            delayMillis: Long = 300L,
            action: suspend () -> Unit,
        ) {
            jobs[key]?.cancel()
            jobs[key] = coroutineScope.launch {
                delay(delayMillis)
                action()
            }
        }
    }
}
