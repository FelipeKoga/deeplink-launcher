package dev.koga.deeplinklauncher.settings.impl.ui.opensource

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.github.takahirom.roborazzi.annotations.RoboPreviewExclude
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@RoboPreviewExclude
@PreviewLightDark
@Composable
internal fun OpenSourceLicensesScreenPreview() {
    DLLPreviewTheme {
        OpenSourceLicensesScreen(
            onBack = {},
        )
    }
}
