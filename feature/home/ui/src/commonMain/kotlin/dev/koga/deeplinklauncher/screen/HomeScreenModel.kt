package dev.koga.deeplinklauncher.screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.provider.UUIDProvider
import dev.koga.deeplinklauncher.usecase.GetDeepLinksAndFolderStream
import dev.koga.deeplinklauncher.usecase.deeplink.GetDeepLinkByLink
import dev.koga.deeplinklauncher.usecase.deeplink.LaunchDeepLink
import dev.koga.deeplinklauncher.usecase.deeplink.LaunchDeepLinkResult
import dev.koga.deeplinklauncher.usecase.deeplink.UpsertDeepLink
import dev.koga.deeplinklauncher.usecase.folder.UpsertFolder
import dev.koga.deeplinklauncher.util.currentLocalDateTime
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenModel(
    getDeepLinksAndFolderStream: GetDeepLinksAndFolderStream,
    private val upsertDeepLink: UpsertDeepLink,
    private val getDeepLinkByLink: GetDeepLinkByLink,
    private val launchDeepLink: LaunchDeepLink,
    private val upsertFolder: UpsertFolder,
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
        errorMessage
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

    private fun insertDeepLink(link: String) {
        screenModelScope.launch {
            upsertDeepLink(
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

        val deepLink = getDeepLinkByLink(link)

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
                    "Something went wrong. Check if the deeplink \"$link\" is valid"
                }
            }
        }
    }

    fun launchDeepLink(deepLink: DeepLink) {
        launchDeepLink.launch(deepLink)
    }

    fun onDeepLinkTextChanged(text: String) {
        errorMessage.update { null }
        inputText.update { text }
    }

    fun addFolder(name: String, description: String) {
        upsertFolder(
            Folder(
                id = UUIDProvider.get(),
                name = name,
                description = description,
                deepLinkCount = 0,
            ),
        )
    }
}