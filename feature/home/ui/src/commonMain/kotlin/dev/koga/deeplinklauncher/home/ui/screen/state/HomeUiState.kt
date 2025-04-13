package dev.koga.deeplinklauncher.home.ui.screen.state

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.model.Suggestion
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeUiState(
    val deepLinks: ImmutableList<DeepLink> = persistentListOf(),
    val favorites: ImmutableList<DeepLink> = persistentListOf(),
    val folders: ImmutableList<Folder> = persistentListOf(),
    val suggestions: ImmutableList<Suggestion> = persistentListOf(),
    val errorMessage: String? = null,
    val deepLinkInput: String = "",
    val searchInput: String = "",
)
