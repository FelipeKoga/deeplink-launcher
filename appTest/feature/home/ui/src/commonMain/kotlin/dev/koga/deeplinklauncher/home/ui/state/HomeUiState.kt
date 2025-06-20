package dev.koga.deeplinklauncher.home.ui.state

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.model.Suggestion
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeUiState(
    val deepLinks: ImmutableList<DeepLink> = persistentListOf(),
    val favorites: ImmutableList<DeepLink> = persistentListOf(),
    val folders: ImmutableList<Folder> = persistentListOf(),
    val deepLinkInputState: DeepLinkInputState = DeepLinkInputState(),
    val searchInput: String = "",
    val showOnboarding: Boolean = false,
)

data class DeepLinkInputState(
    val text: String = "",
    val errorMessage: String? = null,
    val suggestions: List<Suggestion> = persistentListOf(),
)
