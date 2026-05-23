package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.state

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Suggestion
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkListItem
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkInputState
import kotlinx.collections.immutable.ImmutableList

internal data class FolderDetailsUiState(
    val name: String,
    val description: String,
    val deepLinks: ImmutableList<DeepLinkListItem>,
    val deepLinkInputState: DeepLinkInputState = DeepLinkInputState(),
    val pendingLinkConfirmation: DeepLink? = null,
)
