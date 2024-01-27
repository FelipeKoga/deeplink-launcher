package dev.koga.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed interface SharedScreen : ScreenProvider {
    data object ExportDeepLinks : SharedScreen
    data object ImportDeepLinks : SharedScreen
    data class DeepLinkDetails(val id: String) : SharedScreen
    data class FolderDetails(val id: String) : SharedScreen
}