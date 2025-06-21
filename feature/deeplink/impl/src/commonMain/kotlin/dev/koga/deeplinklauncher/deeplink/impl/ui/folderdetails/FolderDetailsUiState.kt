package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import kotlinx.collections.immutable.ImmutableList

internal data class FolderDetailsUiState(
    val name: String,
    val description: String,
    val deepLinks: ImmutableList<DeepLink>,
)
