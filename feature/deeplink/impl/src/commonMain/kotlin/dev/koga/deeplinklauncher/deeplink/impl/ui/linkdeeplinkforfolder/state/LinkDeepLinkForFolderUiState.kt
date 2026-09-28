package dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder.state

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkInputState
import dev.koga.deeplinklauncher.deeplink.uicomponent.model.DeepLinkListItem
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
