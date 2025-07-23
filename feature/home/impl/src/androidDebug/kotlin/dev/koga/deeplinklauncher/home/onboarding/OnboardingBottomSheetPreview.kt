package dev.koga.deeplinklauncher.home.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun OnboardingBottomSheetPreview() {
    DLLPreviewTheme {
        OnboardingBottomSheet(
            onDismiss = {},
        )
    }
}
