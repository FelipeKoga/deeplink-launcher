package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

private val previewMetadata = DeepLinkMetadata(
    scheme = "https",
    host = "google.com",
    path = "/search",
    query = "q=compose",
)

private val previewHandlerInfo = DeepLinkHandlerInfo.Available(
    canResolve = true,
    appName = "Chrome",
)

@Preview
@PreviewLightDark
@Composable
internal fun DetailsInformationContentPreview() {
    DLLPreviewTheme {
        DetailsInformationContent(
            metadata = previewMetadata,
            handlerInfo = previewHandlerInfo,
            icon = null,
        )
    }
}

@Preview
@PreviewLightDark
@Composable
internal fun DetailsInformationContentCannotResolvePreview() {
    DLLPreviewTheme {
        DetailsInformationContent(
            metadata = previewMetadata,
            handlerInfo = DeepLinkHandlerInfo.Available(canResolve = false, appName = null),
            icon = null,
        )
    }
}

@Preview
@PreviewLightDark
@Composable
internal fun DetailsInformationContentUnavailablePreview() {
    DLLPreviewTheme {
        DetailsInformationContent(
            metadata = previewMetadata,
            handlerInfo = DeepLinkHandlerInfo.Unavailable,
            icon = null,
        )
    }
}
