package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewLightDark
@Composable
internal fun DetailsQuickActionsGridPreview() {
    DLLPreviewTheme {
        DetailsQuickActions(
            isFavorite = false,
            onAction = {},
            onShowDeleteConfirmation = {},
        )
    }
}

@Preview
@PreviewLightDark
@Composable
internal fun DetailsQuickActionsGridFavoritePreview() {
    DLLPreviewTheme {
        DetailsQuickActions(
            isFavorite = true,
            onAction = {},
            onShowDeleteConfirmation = {},
        )
    }
}
