package dev.koga.deeplinklauncher.deeplink.api.ui.model

import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

public data class FolderListItem(
    val folder: Folder,
    val previewIcons: ImmutableList<DeepLinkIcon?> = persistentListOf(),
)
