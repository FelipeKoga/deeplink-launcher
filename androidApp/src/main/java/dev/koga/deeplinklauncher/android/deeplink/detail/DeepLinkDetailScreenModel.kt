package dev.koga.deeplinklauncher.android.deeplink.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.deeplinklauncher.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.usecase.CopyToClipboard
import dev.koga.deeplinklauncher.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.usecase.ShareDeepLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DeepLinkDetails(
    val id: String,
    val name: String,
    val description: String,
    val folder: Folder?,
    val link: String,
    val isFavorite: Boolean,
    val deleted: Boolean,
)

class DeepLinkDetailScreenModel(
    private val deepLinkId: String,
    private val deepLinkRepository: DeepLinkRepository,
    private val launchDeepLink: LaunchDeepLink,
    private val shareDeepLink: ShareDeepLink,
) : ScreenModel {

    private lateinit var deepLink: DeepLink

    val details = MutableStateFlow(
        DeepLinkDetails(
            id = "",
            name = "",
            description = "",
            folder = null,
            link = "",
            isFavorite = false,
            deleted = false,
        )
    )

    init {
        screenModelScope.launch {
            val data = deepLinkRepository.getDeepLinkById(deepLinkId).first()!!

            deepLink = data

            details.update {
                it.copy(
                    name = data.name.orEmpty(),
                    description = data.description.orEmpty(),
                    folder = data.folder,
                    link = data.link,
                    isFavorite = data.isFavorite,
                )
            }
        }

    }

    fun updateDeepLinkDescription(s: String) {
        details.update { it.copy(description = s) }

        deepLinkRepository.upsert(
            deepLink.copy(description = s)
        )
    }

    fun updateDeepLinkName(s: String) {
        details.update { it.copy(name = s) }

        deepLinkRepository.upsert(
            deepLink.copy(name = s)
        )
    }

    fun favorite() {
        screenModelScope.launch {
            details.update { it.copy(isFavorite = !it.isFavorite) }

            deepLinkRepository.toggleFavoriteDeepLink(
                deepLinkId = deepLink.id,
                isFavorite = !deepLink.isFavorite
            )
        }
    }

    fun launch() {
        launchDeepLink.launch(deepLink.link)
    }

    fun delete() {
        screenModelScope.launch {
            details.update { it.copy(deleted = true) }

            deepLinkRepository.deleteDeeplink(deepLink)
        }
    }

    fun share() {
        shareDeepLink(deepLink)
    }

    fun copy() {
    }


}