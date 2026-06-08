package dev.koga.deeplinklauncher.deeplink.api.ui.navigation

import dev.koga.deeplinklauncher.navigation.AppRoute
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

public sealed interface DeepLinkRouteEntryPoint : AppRoute {

    @Serializable
    public data object AddFolder : DeepLinkRouteEntryPoint {
        @Transient
        override val analyticsScreenName: String = "add_folder"
    }

    @Serializable
    public data class FolderDetails(val id: String) : DeepLinkRouteEntryPoint {
        @Transient
        override val analyticsScreenName: String = "folder_details"
    }

    @Serializable
    public data class PickDeepLinkForFolder(val folderId: String) : DeepLinkRouteEntryPoint {
        @Transient
        override val analyticsScreenName: String = "pick_deeplink_for_folder"
    }

    @Serializable
    public data class DeepLinkDetails(val id: String, val showFolder: Boolean) : DeepLinkRouteEntryPoint {
        @Transient
        override val analyticsScreenName: String = "deeplink_details"
    }
}
