package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@PreviewLightDark
@Composable
internal fun DetailsQuickActionsGridInactivePreview() {
    DLLPreviewTheme {
        DetailsQuickActions(
            isFavorite = false,
            isShortcut = false,
            onAction = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun DetailsQuickActionsGridActivePreview() {
    DLLPreviewTheme {
        DetailsQuickActions(
            isFavorite = true,
            isShortcut = true,
            onAction = {},
        )
    }
}
