package dev.koga.deeplinklauncher.navigation

import kotlinx.serialization.Serializable

public sealed interface AppNavigationRoute {

    @Serializable
    public data object Back : AppNavigationRoute

    @Serializable
    public data object Home : AppNavigationRoute

    @Serializable
    public data class DeepLinkDetails(val id: String, val showFolder: Boolean) : AppNavigationRoute

    @Serializable
    public data class FolderDetails(val id: String) : AppNavigationRoute

    @Serializable
    public data object ImportData : AppNavigationRoute

    @Serializable
    public data object ExportData : AppNavigationRoute

    public sealed interface Settings : AppNavigationRoute {
        @Serializable
        public data object Root : Settings

        @Serializable
        public data object OpenSourceLicenses : Settings

        @Serializable
        public data object AppThemeBottomSheet : Settings

        @Serializable
        public data object SuggestionsOptionBottomSheet : Settings

        @Serializable
        public data object DeleteDataBottomSheet : Settings
    }
}
