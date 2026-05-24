package dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder.state

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkListItem
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkInputState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class LinkDeepLinkForFolderUiState(
    val folderName: String,
    val query: String = "",
    val linkableDeepLinks: ImmutableList<DeepLinkListItem> = persistentListOf(),
    val deepLinkInputState: DeepLinkInputState = DeepLinkInputState(),
    val pendingLinkConfirmation: DeepLink? = null,
)
