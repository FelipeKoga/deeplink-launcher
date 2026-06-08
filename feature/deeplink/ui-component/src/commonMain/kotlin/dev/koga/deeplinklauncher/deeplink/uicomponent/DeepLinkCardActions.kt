package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.runtime.Immutable

@Immutable
data class DeepLinkCardActions(
    val onLaunch: (() -> Unit)? = null,
    val onToggleFavorite: (() -> Unit)? = null,
) {
    val hasActions: Boolean
        get() = onLaunch != null || onToggleFavorite != null
}

object DeepLinkCardActionsPresets {
    fun browse(
        onLaunch: () -> Unit,
        onToggleFavorite: () -> Unit,
    ): DeepLinkCardActions = DeepLinkCardActions(
        onLaunch = onLaunch,
        onToggleFavorite = onToggleFavorite,
    )

    fun folderMember(onLaunch: () -> Unit): DeepLinkCardActions = DeepLinkCardActions(
        onLaunch = onLaunch,
    )

    val linkPicker: DeepLinkCardActions = DeepLinkCardActions()
}
