package dev.koga.deeplinklauncher.settings.impl.opensource

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun OpenSourceLicensesScreenPreview() {
    DLLPreviewTheme {
        OpenSourceLicensesScreen(
            onBack = {}
        )
    }
}