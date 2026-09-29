package dev.koga.deeplinklauncher.datatransfer.impl.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.import.jsonStructurePreview
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme

@PreviewLightDark
@Composable
private fun JSONBoxViewerPreview() {
    DLLPreviewTheme {
        JSONBoxViewer(
            text = jsonStructurePreview,
        )
    }
}
