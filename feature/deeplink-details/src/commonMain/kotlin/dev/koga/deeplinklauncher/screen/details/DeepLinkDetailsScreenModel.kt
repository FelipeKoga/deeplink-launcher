package dev.koga.deeplinklauncher.screen.details

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.FolderDataSource
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.model.isLinkValid
import dev.koga.deeplinklauncher.provider.UUIDProvider
import dev.koga.deeplinklauncher.screen.details.event.DeepLinkDetailsEvent
import dev.koga.deeplinklauncher.screen.details.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.usecase.DuplicateDeepLink
import dev.koga.deeplinklauncher.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.usecase.ShareDeepLink
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

class DeepLinkDetailsScreenModel(
    deepLinkId: String,
    private val folderDataSource: FolderDataSource,
    private val deepLinkDataSource: DeepLinkDataSource,
    private val launchDeepLink: LaunchDeepLink,
    private val shareDeepLink: ShareDeepLink,
    private val duplicateDeepLink: DuplicateDeepLink,
) : ScreenModel {

    private val coroutineDebouncer = CoroutineDebouncer()

    private val deepLink = deepLinkDataSource.getDeepLinkByIdStream(deepLinkId)
        .filterNotNull()
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DeepLink.empty,
        )

    private val folders = folderDataSource.getFoldersStream().stateIn(
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
            deepLinkDataSource.upsertDeepLink(deepLink.value.copy(link = link))

            if (!link.isLinkValid) {
                deepLinkErrorMessage.update { "Invalid deeplink" }
            }
        }
    }

    fun updateName(name: String) {
        coroutineDebouncer.debounce(screenModelScope, "name") {
            deepLinkDataSource.upsertDeepLink(deepLink.value.copy(name = name))
        }
    }

    fun updateDescription(description: String) {
        coroutineDebouncer.debounce(screenModelScope, "description") {
            deepLinkDataSource.upsertDeepLink(deepLink.value.copy(description = description))
        }
    }

    fun toggleFavorite() {
        deepLinkDataSource.upsertDeepLink(
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
            deepLinkDataSource.deleteDeepLink(deepLink.value.id)
            eventDispatcher.send(DeepLinkDetailsEvent.Deleted)
        }
    }

    fun share() {
        shareDeepLink(deepLink.value)
    }

    fun insertFolder(name: String, description: String) {
        val folder = Folder(
            id = UUIDProvider.get(),
            name = name,
            description = description,
        )

        folderDataSource.upsertFolder(folder)

        selectFolder(folder)
    }

    fun selectFolder(folder: Folder) {
        deepLinkDataSource.upsertDeepLink(deepLink.value.copy(folder = folder))
    }

    fun removeFolderFromDeepLink() {
        deepLinkDataSource.upsertDeepLink(deepLink.value.copy(folder = null))
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
                DuplicateDeepLink.Response.Error.InvalidLink -> {
                    duplicateErrorMessage.update {
                        "No app found to handle this deep link: $newLink"
                    }
                }

                DuplicateDeepLink.Response.Error.LinkAlreadyExists -> {
                    duplicateErrorMessage.update { "Link already exists" }
                }

                DuplicateDeepLink.Response.Error.SameLink -> {
                    duplicateErrorMessage.update { "Link is the same as the original one" }
                }

                is DuplicateDeepLink.Response.Success -> {
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
