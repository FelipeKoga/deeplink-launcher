package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DetailsBottomActionsFavoritePreview() {
    DLLPreviewTheme {
        DetailsBottomActions(
            isFavorite = true,
            onAction = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun DetailsBottomActionsNotFavoritePreview() {
    DLLPreviewTheme {
        DetailsBottomActions(
            isFavorite = false,
            onAction = {},
        )
    }
}
