package dev.koga.deeplinklauncher.screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.FolderDataSource
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.provider.UUIDProvider
import dev.koga.deeplinklauncher.screen.state.HomeEvent
import dev.koga.deeplinklauncher.screen.state.HomeUiState
import dev.koga.deeplinklauncher.usecase.GetDeepLinksAndFolderStream
import dev.koga.deeplinklauncher.usecase.deeplink.LaunchDeepLink
import dev.koga.deeplinklauncher.usecase.deeplink.LaunchDeepLinkResult
import dev.koga.deeplinklauncher.util.ext.currentLocalDateTime
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenModel(
    getDeepLinksAndFolderStream: GetDeepLinksAndFolderStream,
    private val deepLinkDataSource: DeepLinkDataSource,
    private val folderDataSource: FolderDataSource,
    private val launchDeepLink: LaunchDeepLink,
) : ScreenModel {

    private val inputText = MutableStateFlow("")
    private val dataStream = getDeepLinksAndFolderStream().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GetDeepLinksAndFolderStream.Output(
            deepLinks = emptyList(),
            favorites = emptyList(),
            folders = emptyList(),
        ),
    )
    private val errorMessage = MutableStateFlow<String?>(null)

    val uiState = combine(
        inputText,
        dataStream,
        errorMessage,
    ) { deepLinkText, dataStream, errorMessage ->
        HomeUiState(
            inputText = deepLinkText,
            deepLinks = dataStream.deepLinks.toPersistentList(),
            favorites = dataStream.favorites.toPersistentList(),
            folders = dataStream.folders.toPersistentList(),
            errorMessage = errorMessage,
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = HomeUiState(),
    )

    private val eventDispatcher = Channel<HomeEvent>(Channel.UNLIMITED)
    val events = eventDispatcher.receiveAsFlow()

    private fun insertDeepLink(link: String) {
        screenModelScope.launch {
            deepLinkDataSource.upsertDeepLink(
                DeepLink(
                    id = UUIDProvider.get(),
                    link = link,
                    name = null,
                    description = null,
                    folder = null,
                    isFavorite = false,
                    lastLaunchedAt = currentLocalDateTime,
                ),
            )
        }
    }

    fun launchDeepLink() = screenModelScope.launch {
        val link = inputText.value

        val deepLink = deepLinkDataSource.getDeepLinkByLink(link)

        if (deepLink != null) {
            launchDeepLink(deepLink)

            return@launch
        }

        when (launchDeepLink.launch(link)) {
            is LaunchDeepLinkResult.Success -> {
                insertDeepLink(link)
            }

            is LaunchDeepLinkResult.Failure -> {
                errorMessage.update {
                    "No app found to handle this deep link: $link"
                }
            }
        }
    }

    fun launchDeepLink(deepLink: DeepLink) {
        screenModelScope.launch {
            launchDeepLink.launch(deepLink)

            eventDispatcher.send(HomeEvent.DeepLinksLaunched)
        }
    }

    fun onDeepLinkTextChanged(text: String) {
        errorMessage.update { null }
        inputText.update { text }
    }

    fun addFolder(name: String, description: String) {
        folderDataSource.upsertFolder(
            Folder(
                id = UUIDProvider.get(),
                name = name,
                description = description,
                deepLinkCount = 0,
            ),
        )
    }
}
