package dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder.state

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Suggestion

internal sealed interface LinkDeepLinkForFolderAction {
    data class QueryChanged(val text: String) : LinkDeepLinkForFolderAction
    data class DeepLinkSelected(val deepLinkId: String) : LinkDeepLinkForFolderAction
    data class Launch(val deeplink: DeepLink) : LinkDeepLinkForFolderAction
    data object LaunchInputDeepLink : LinkDeepLinkForFolderAction
    data class OnInputChanged(val text: String) : LinkDeepLinkForFolderAction
    data class OnSuggestionClicked(val suggestion: Suggestion) : LinkDeepLinkForFolderAction
    data object ConfirmLinkToFolder : LinkDeepLinkForFolderAction
    data object DismissLinkConfirmation : LinkDeepLinkForFolderAction
}
