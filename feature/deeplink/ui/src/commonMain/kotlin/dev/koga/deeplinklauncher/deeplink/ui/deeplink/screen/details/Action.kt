package dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details

import dev.koga.deeplinklauncher.deeplink.api.model.Folder

interface Action

sealed interface LaunchAction : Action {
    data object Share : LaunchAction
    data object Launch : LaunchAction
    data object ToggleFavorite : LaunchAction
    data object Duplicate : LaunchAction
    data object Edit : LaunchAction
    data object Delete : LaunchAction
}

sealed interface DuplicateAction : Action {
    data object Back : DuplicateAction
    data class Duplicate(val newLink: String, val copyAllFields: Boolean) : DuplicateAction
}

sealed interface EditAction : Action {
    data object Back : EditAction
    data class OnNameChanged(val text: String) : EditAction
    data class OnDescriptionChanged(val text: String) : EditAction
    data class OnLinkChanged(val text: String) : EditAction
    data class ToggleFolder(val folder: Folder) : EditAction
    data class AddFolder(val name: String, val description: String) : EditAction
}
