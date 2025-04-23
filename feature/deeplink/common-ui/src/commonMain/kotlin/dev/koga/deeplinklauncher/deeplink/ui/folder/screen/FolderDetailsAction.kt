package dev.koga.deeplinklauncher.deeplink.ui.folder.screen

import dev.koga.deeplinklauncher.deeplink.common.model.DeepLink

sealed interface FolderDetailsAction {
    data class UpdateName(val text: String) : FolderDetailsAction
    data class UpdateDescription(val text: String) : FolderDetailsAction
    data class Launch(val deeplink: DeepLink) : FolderDetailsAction
    data object Delete : FolderDetailsAction
}