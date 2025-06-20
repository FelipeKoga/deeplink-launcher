package dev.koga.deeplinklauncher.deeplink.ui.folder.screen.details

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink

sealed interface FolderDetailsAction {
    data class UpdateName(val text: String) : FolderDetailsAction
    data class UpdateDescription(val text: String) : FolderDetailsAction
    data class Launch(val deeplink: DeepLink) : FolderDetailsAction
    data object Delete : FolderDetailsAction
}
