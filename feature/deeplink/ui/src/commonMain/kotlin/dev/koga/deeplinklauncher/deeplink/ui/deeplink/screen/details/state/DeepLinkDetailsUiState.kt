package dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.state

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import kotlinx.collections.immutable.ImmutableList

sealed interface DeepLinkDetailsUiState {
    val deepLink: DeepLink

    data class Launch(
        override val deepLink: DeepLink
    ): DeepLinkDetailsUiState

    data class Edit(
        override val deepLink: DeepLink,
        val folders: ImmutableList<Folder>,
        val errorMessage: String? = null,
    ): DeepLinkDetailsUiState

    data class Duplicate(
        override val deepLink: DeepLink,
        val errorMessage: String? = null,
    ): DeepLinkDetailsUiState
}