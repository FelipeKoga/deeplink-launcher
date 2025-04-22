package dev.koga.deeplinklauncher.deeplink.ui.folder.screen.details

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import kotlinx.collections.immutable.ImmutableList

data class FolderDetailsUiState(
    val name: String,
    val description: String,
    val deepLinks: ImmutableList<DeepLink>,
)
