package dev.koga.deeplinklauncher.android.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.LaunchDeepLink
import dev.koga.deeplinklauncher.LaunchDeepLinkResult
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.repository.DeepLinkRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenModel(
    private val repository: DeepLinkRepository,
    private val launchDeepLink: LaunchDeepLink,
) : ScreenModel {

    val searchText = MutableStateFlow("")

    val deepLinks = searchText.flatMapLatest {
        repository.getAllDeepLinks(it)
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
            repository.insertDeeplink(
                DeepLink(
                    id = UUID.randomUUID().toString(),
                    link = link,
                    name = null,
                    description = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = null,
                    folder = Folder(
                        id = UUID.randomUUID().toString(),
                        name = "Folder ${Random.nextInt()}",
                        description = null,
                        color = null
                    ),
                    isFavorite = false
                )
            )
        }
    }

    fun launchDeepLink() = screenModelScope.launch {
        val link = deepLinkText.value

        val deepLink = repository.getDeepLinkByLink(link).firstOrNull()

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
            repository.deleteDeeplink(deepLink)
        }
    }

    fun share() {
        val deepLink = selectedDeepLink.value ?: return

    }

    fun favorite() {
        val deepLink = selectedDeepLink.value ?: return

        screenModelScope.launch {
            repository.toggleFavoriteDeepLink(
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