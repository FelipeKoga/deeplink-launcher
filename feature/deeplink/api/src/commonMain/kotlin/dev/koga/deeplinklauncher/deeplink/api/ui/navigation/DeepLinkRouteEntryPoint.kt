package dev.koga.deeplinklauncher.deeplink.api.ui.navigation

import dev.koga.deeplinklauncher.navigation.AppRoute
import kotlinx.serialization.Serializable

public sealed interface DeepLinkRouteEntryPoint : AppRoute {

    @Serializable
    public data object AddFolder : DeepLinkRouteEntryPoint

    @Serializable
    public data class FolderDetails(val id: String) : DeepLinkRouteEntryPoint

    @Serializable
    public data class DeepLinkDetails(val id: String, val showFolder: Boolean) : DeepLinkRouteEntryPoint
}
