package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun DeepLinkCardPreview() {
    DLLPreviewTheme {
        DeepLinkCard(
            deepLink = DeepLink(
                id = "1",
                link = "https://www.google.com",
                name = "Google",
                description = "Search engine",
                isFavorite = false,
            ),
            onClick = {},
            onLaunch = {},
            onFolderClicked = {},
            showFolder = false
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun DeepLinkCardShowFolderPreview() {
    DLLPreviewTheme {
        DeepLinkCard(
            deepLink = DeepLink(
                id = "1",
                link = "https://www.google.com",
                name = "Google",
                description = "Search engine",
                isFavorite = false,
                folder = Folder(
                    id = "1",
                    name = "Folder 1",
                    description = "Folder 1 description",
                    deepLinkCount = 1
                )
            ),
            onClick = {},
            onLaunch = {},
            onFolderClicked = {},
            showFolder = true
        )
    }
}