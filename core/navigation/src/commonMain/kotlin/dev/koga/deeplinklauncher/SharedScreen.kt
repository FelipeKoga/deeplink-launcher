package dev.koga.deeplinklauncher

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface SharedScreen : ScreenProvider {
    data object ExportDeepLinks : SharedScreen
    data object ImportDeepLinks : SharedScreen
    data class DeepLinkDetails(val id: String, val showFolder: Boolean) : SharedScreen {
        sealed interface Result {
            data class NavigateToFolderDetails(val id: String) : Result
            data class NavigateToDuplicated(val id: String) : Result

            companion object {
                const val KEY = "deeplink_details_result"
            }
        }
    }

    data class FolderDetails(val id: String) : SharedScreen
    data object Settings : SharedScreen
}
