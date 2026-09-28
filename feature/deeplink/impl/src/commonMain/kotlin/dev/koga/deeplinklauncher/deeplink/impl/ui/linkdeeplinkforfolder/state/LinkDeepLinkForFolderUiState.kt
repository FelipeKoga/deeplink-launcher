package dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder.state

import dev.koga.deeplinklauncher.deeplink.ui.DeepLinkInputState
import dev.koga.deeplinklauncher.deeplink.ui.model.DeepLinkListItem
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class LinkDeepLinkForFolderUiState(
    val folderName: String,
    val isFolderLoaded: Boolean = false,
    val query: String = "",
    val linkableDeepLinks: ImmutableList<DeepLinkListItem> = persistentListOf(),
    val deepLinkInputState: DeepLinkInputState = DeepLinkInputState(),
    val pendingLinkConfirmation: DeepLink? = null,
)
