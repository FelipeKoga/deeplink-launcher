package dev.koga.deeplinklauncher.settings.impl.ui.apptheme

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.preferences.model.AppTheme

@PreviewLightDark
@Composable
internal fun AppThemeBottomSheetContentLightSelectedPreview() {
    DLLPreviewTheme {
        AppThemeBottomSheetContent(
            appTheme = AppTheme.LIGHT,
            onSelect = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun AppThemeBottomSheetContentDarkSelectedPreview() {
    DLLPreviewTheme {
        AppThemeBottomSheetContent(
            appTheme = AppTheme.DARK,
            onSelect = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun AppThemeBottomSheetContentAutoSelectedPreview() {
    DLLPreviewTheme {
        AppThemeBottomSheetContent(
            appTheme = AppTheme.AUTO,
            onSelect = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun AppThemeListItemSelectedPreview() {
    DLLPreviewTheme {
        AppThemeListItem(
            label = "Label",
            selected = true,
            onClick = {},
        )
    }
}

@PreviewLightDark
@Composable
internal fun AppThemeListItemNotSelectedPreview() {
    DLLPreviewTheme {
        AppThemeListItem(
            label = "Label",
            selected = false,
            onClick = {},
        )
    }
}
