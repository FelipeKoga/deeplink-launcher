package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkListItem
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun DeepLinkCardPreview() {
    DLLPreviewTheme {
        DeepLinkCard(
            item = DeepLinkListItem(deepLink = DeepLink.previewNotFavorite),
            onClick = {},
            onLaunch = {},
            onFolderClicked = {},
            showFolder = false,
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
            item = DeepLinkListItem(deepLink = DeepLink.previewFavorite),
            onClick = {},
            onLaunch = {},
            onFolderClicked = {},
            showFolder = true,
        )
    }
}
