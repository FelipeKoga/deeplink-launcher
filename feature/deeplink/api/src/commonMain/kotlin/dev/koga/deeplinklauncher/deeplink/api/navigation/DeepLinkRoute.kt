package dev.koga.deeplinklauncher.deeplink.api.navigation

import dev.koga.deeplinklauncher.navigation.AppRoute
import kotlinx.serialization.Serializable

public sealed interface DeepLinkRoute : AppRoute {

    @Serializable
    public data object AddFolder : DeepLinkRoute

    @Serializable
    public data class FolderDetails(val id: String) : DeepLinkRoute

    @Serializable
    public data class DeepLinkDetails(val id: String, val showFolder: Boolean) : DeepLinkRoute
}
