package dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.state

import dev.koga.deeplinklauncher.deeplink.api.model.Folder

sealed interface DeepLinkDetailsAction

sealed interface LaunchAction : DeepLinkDetailsAction {
    data object Share : LaunchAction
    data object Launch : LaunchAction
    data object ToggleFavorite : LaunchAction
    data object Duplicate : LaunchAction
    data object Edit : LaunchAction
    data object NavigateToFolder : LaunchAction
}

sealed interface DuplicateAction : DeepLinkDetailsAction {
    data object Back : DuplicateAction
    data class Duplicate(val newLink: String, val copyAllFields: Boolean) : DuplicateAction
}

sealed interface EditAction : DeepLinkDetailsAction {
    data object Back : EditAction
    data object Delete : EditAction
    data class OnNameChanged(val text: String) : EditAction
    data class OnDescriptionChanged(val text: String) : EditAction
    data class OnLinkChanged(val text: String) : EditAction
    data class ToggleFolder(val folder: Folder) : EditAction
    data object AddFolder : EditAction
}
