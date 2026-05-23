package dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.state

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Suggestion

internal sealed interface FolderDetailsAction {
    data class UpdateName(val text: String) : FolderDetailsAction
    data class UpdateDescription(val text: String) : FolderDetailsAction
    data class Launch(val deeplink: DeepLink) : FolderDetailsAction
    data object LaunchInputDeepLink : FolderDetailsAction
    data class OnInputChanged(val text: String) : FolderDetailsAction
    data class OnSuggestionClicked(val suggestion: Suggestion) : FolderDetailsAction
    data object ConfirmLinkToFolder : FolderDetailsAction
    data object DismissLinkConfirmation : FolderDetailsAction
    data object Delete : FolderDetailsAction
}
