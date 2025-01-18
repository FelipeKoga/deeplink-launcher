package dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.state

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import kotlinx.collections.immutable.ImmutableList

data class DeepLinkDetailsUiState(
    val folders: ImmutableList<Folder>,
    val deepLink: DeepLink,
    val duplicateErrorMessage: String? = null,
    val deepLinkErrorMessage: String? = null,
)
