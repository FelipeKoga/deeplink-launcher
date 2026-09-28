package dev.koga.deeplinklauncher.deeplink.ui
import androidx.compose.runtime.Immutable

@Immutable
public data class DeepLinkCardActions(
    val onLaunch: (() -> Unit)? = null,
    val onToggleFavorite: (() -> Unit)? = null,
) {
    public val hasActions: Boolean
        get() = onLaunch != null || onToggleFavorite != null
}

public object DeepLinkCardActionsPresets {
    public fun browse(
        onLaunch: () -> Unit,
        onToggleFavorite: () -> Unit,
    ): DeepLinkCardActions = DeepLinkCardActions(
        onLaunch = onLaunch,
        onToggleFavorite = onToggleFavorite,
    )

    public fun folderMember(onLaunch: () -> Unit): DeepLinkCardActions = DeepLinkCardActions(
        onLaunch = onLaunch,
    )

    public val linkPicker: DeepLinkCardActions = DeepLinkCardActions()
}
