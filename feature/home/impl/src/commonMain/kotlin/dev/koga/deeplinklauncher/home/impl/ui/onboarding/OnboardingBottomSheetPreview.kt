package dev.koga.deeplinklauncher.home.impl.ui.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@PreviewLightDark
@Composable
internal fun OnboardingBottomSheetPreview() {
    DLLPreviewTheme {
        OnboardingBottomSheet(
            onDismiss = {},
        )
    }
}
