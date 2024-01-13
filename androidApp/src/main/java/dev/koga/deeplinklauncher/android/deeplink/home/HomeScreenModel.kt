package dev.koga.deeplinklauncher.android.deeplink.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.usecase.LaunchDeepLinkResult
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.repository.FolderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenModel(
    private val deepLinkRepository: DeepLinkRepository,
    private val launchDeepLink: LaunchDeepLink,
    private val folderRepository: FolderRepository,
) : ScreenModel {

    val deepLinkText = MutableStateFlow("")
    val searchText = MutableStateFlow("")

    val deepLinks = searchText.flatMapLatest {
        deepLinkRepository.getAllDeepLinks(it)
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val favoriteDeepLinks = deepLinks.mapLatest { deepLinks ->
        deepLinks.filter(DeepLink::isFavorite)
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val folders = folderRepository.getFolders().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )


    private val dispatchErrorMessage = MutableStateFlow<String?>(null)
    val errorMessage = dispatchErrorMessage.asStateFlow()

    private fun insertDeepLink(link: String) {
        screenModelScope.launch {
            deepLinkRepository.upsert(
                DeepLink(
                    id = UUID.randomUUID().toString(),
                    link = link,
                    name = null,
                    description = null,
                    createdAt = System.currentTimeMillis(),
                    folder = null,
                    isFavorite = false
                )
            )
        }
    }

    fun launchDeepLink() = screenModelScope.launch {
        val link = deepLinkText.value

        val deepLink = deepLinkRepository.getDeepLinkByLink(link).firstOrNull()

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
        launchDeepLink.launch(deepLink.link)
    }

    fun onDeepLinkTextChanged(text: String) {
        dispatchErrorMessage.update { null }
        deepLinkText.update { text }
    }

    fun onSearchTextChanged(text: String) {
        searchText.update { text }
    }

    fun addFolder(name: String, description: String) {
        folderRepository.upsertFolder(
            Folder(
                id = UUID.randomUUID().toString(),
                name = name,
                description = description,
                color = null,
            )
        )
    }


}