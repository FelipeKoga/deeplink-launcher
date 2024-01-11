package dev.koga.deeplinklauncher.android.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.usecase.LaunchDeepLinkResult
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.repository.FolderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
    private val folderRepository: FolderRepository,
    private val launchDeepLink: LaunchDeepLink,
) : ScreenModel {

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

    val deepLinkText = MutableStateFlow("")

    private val selectedDeepLinkId = MutableStateFlow<String?>(null)

    val selectedDeepLink = combine(selectedDeepLinkId, deepLinks) { deepLinkId, deepLinks ->
        deepLinks.firstOrNull { it.id == deepLinkId }
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    private val dispatchErrorMessage = MutableStateFlow<String?>(null)
    val errorMessage = dispatchErrorMessage.asStateFlow()

    fun selectDeepLink(deepLink: DeepLink) {
        selectedDeepLinkId.update { deepLink.id }
    }

    fun clearSelectedDeepLink() {
        selectedDeepLinkId.update { null }
    }

    private fun insertDeepLink(link: String) {
        screenModelScope.launch {
            deepLinkRepository.insertDeeplink(
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

    fun delete() {
        val deepLink = selectedDeepLink.value ?: return

        selectedDeepLinkId.update { null }

        screenModelScope.launch {
            deepLinkRepository.deleteDeeplink(deepLink)
        }
    }

    fun share() {
        val deepLink = selectedDeepLink.value ?: return

    }

    fun favorite() {
        val deepLink = selectedDeepLink.value ?: return

        screenModelScope.launch {
            deepLinkRepository.toggleFavoriteDeepLink(
                deepLinkId = deepLink.id,
                isFavorite = !deepLink.isFavorite
            )
        }
    }

    fun onDeepLinkTextChanged(text: String) {
        dispatchErrorMessage.update { null }
        deepLinkText.update { text }
    }

    fun onSearchTextChanged(text: String) {
        searchText.update { text }
    }
}