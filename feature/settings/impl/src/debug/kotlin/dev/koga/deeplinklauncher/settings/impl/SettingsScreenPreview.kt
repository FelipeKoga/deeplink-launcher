package dev.koga.deeplinklauncher.settings.impl

import androidx.compose.material3.Text
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
internal fun SettingsScreenPreview() {
    DLLPreviewTheme {
        SettingsScreen(
            viewmodel = koinViewModel(),
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun SettingsUIPurchaseAvailablePreview() {
    DLLPreviewTheme {
        SettingsUI(
            isPurchaseAvailable = true,
            onBack = {},
            onNavigateToExport = {},
            onNavigateToImport = {},
            onNavigateToStore = {},
            onNavigateToOpenSourceLicenses = {},
            onNavigateToGithub = {},
            onShowDeleteDataBottomSheet = {},
            onShowAppTheme = {},
            onShowSuggestionsOption = {},
            onShowProducts = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun SettingsUIPurchaseNotAvailablePreview() {
    DLLPreviewTheme {
        SettingsUI(
            isPurchaseAvailable = false,
            onBack = {},
            onNavigateToExport = {},
            onNavigateToImport = {},
            onNavigateToStore = {},
            onNavigateToOpenSourceLicenses = {},
            onNavigateToGithub = {},
            onShowDeleteDataBottomSheet = {},
            onShowAppTheme = {},
            onShowSuggestionsOption = {},
            onShowProducts = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
internal fun SettingsListItemPreview() {
    DLLPreviewTheme {
        SettingsListItem(
            title = "Title",
            description = "Description",
            trailingContent = {
                Text("Trailing Content")
            },
            onClick = {},
        )
    }
}
