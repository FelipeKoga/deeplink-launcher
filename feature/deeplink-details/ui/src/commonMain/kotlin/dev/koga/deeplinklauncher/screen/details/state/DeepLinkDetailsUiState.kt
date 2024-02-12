package dev.koga.deeplinklauncher.screen.details.state

import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import kotlinx.collections.immutable.ImmutableList

data class DeepLinkDetailsUiState(
    val folders: ImmutableList<Folder>,
    val deepLink: DeepLink,
    val duplicateErrorMessage: String? = null,
)
