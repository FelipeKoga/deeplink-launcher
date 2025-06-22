package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink

internal sealed interface FolderDetailsAction {
    data class UpdateName(val text: String) : FolderDetailsAction
    data class UpdateDescription(val text: String) : FolderDetailsAction
    data class Launch(val deeplink: DeepLink) : FolderDetailsAction
    data object Delete : FolderDetailsAction
}
