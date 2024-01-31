package dev.koga.deeplinklauncher

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.provider.UUIDProvider
import dev.koga.deeplinklauncher.usecase.deeplink.GetDeepLinkByLink
import dev.koga.deeplinklauncher.usecase.deeplink.GetDeepLinksStream
import dev.koga.deeplinklauncher.usecase.deeplink.LaunchDeepLink
import dev.koga.deeplinklauncher.usecase.deeplink.LaunchDeepLinkResult
import dev.koga.deeplinklauncher.usecase.deeplink.UpsertDeepLink
import dev.koga.deeplinklauncher.usecase.folder.GetFoldersStream
import dev.koga.deeplinklauncher.usecase.folder.UpsertFolder
import dev.koga.deeplinklauncher.util.currentLocalDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenModel(
    getDeepLinksStream: GetDeepLinksStream,
    getFoldersStream: GetFoldersStream,
    private val upsertDeepLink: UpsertDeepLink,
    private val getDeepLinkByLink: GetDeepLinkByLink,
    private val launchDeepLink: LaunchDeepLink,
    private val upsertFolder: UpsertFolder,
) : ScreenModel {

    val deepLinkText = MutableStateFlow("")

    val deepLinks = getDeepLinksStream().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    val favoriteDeepLinks = deepLinks.mapLatest { deepLinks ->
        deepLinks.filter(DeepLink::isFavorite)
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    val folders = getFoldersStream().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    private val dispatchErrorMessage = MutableStateFlow<String?>(null)
    val errorMessage = dispatchErrorMessage.asStateFlow()

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
                ),
            )
        }
    }

    fun launchDeepLink() = screenModelScope.launch {
        val link = deepLinkText.value

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
                dispatchErrorMessage.update {
                    "Something went wrong. Check if the deeplink \"$link\" is valid"
                }
            }
        }
    }

    fun launchDeepLink(deepLink: DeepLink) {
        launchDeepLink.launch(deepLink)
    }

    fun onDeepLinkTextChanged(text: String) {
        dispatchErrorMessage.update { null }
        deepLinkText.update { text }
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
