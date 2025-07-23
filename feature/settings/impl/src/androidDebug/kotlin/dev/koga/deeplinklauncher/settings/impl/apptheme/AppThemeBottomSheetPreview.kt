package dev.koga.deeplinklauncher.settings.impl.apptheme

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun AppThemeBottomSheetPreview() {
    DLLPreviewTheme {
        AppThemeBottomSheet(
            viewModel = koinViewModel(),
            onDismissRequest = {},
        )
    }
}

@Preview
@PreviewScreenSizes
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

@Preview
@PreviewScreenSizes
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
