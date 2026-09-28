package dev.koga.deeplinklauncher.deeplink.ui.model
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkIcon
import dev.koga.deeplinklauncher.domain.deeplink.api.model.Folder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

public data class FolderListItem(
    val folder: Folder,
    val previewIcons: ImmutableList<DeepLinkIcon?> = persistentListOf(),
)
