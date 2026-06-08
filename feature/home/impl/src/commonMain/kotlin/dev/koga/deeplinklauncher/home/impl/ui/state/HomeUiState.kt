package dev.koga.deeplinklauncher.home.impl.ui.state

import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkListItem
import dev.koga.deeplinklauncher.deeplink.api.ui.model.FolderListItem
import dev.koga.deeplinklauncher.deeplink.uicomponent.DeepLinkInputState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeUiState(
    val deepLinks: ImmutableList<DeepLinkListItem> = persistentListOf(),
    val favorites: ImmutableList<DeepLinkListItem> = persistentListOf(),
    val folders: ImmutableList<FolderListItem> = persistentListOf(),
    val deepLinkInputState: DeepLinkInputState = DeepLinkInputState(),
    val searchInput: String = "",
    val showOnboarding: Boolean = false,
)
