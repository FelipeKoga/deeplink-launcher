package dev.koga.deeplinklauncher.datatransfer.impl.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.import.jsonStructurePreview
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun JSONBoxViewerPreview() {
    DLLPreviewTheme {
        JSONBoxViewer(
            text = jsonStructurePreview,
        )
    }
}
