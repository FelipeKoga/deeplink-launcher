package dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.import

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.file.model.FileType
import kotlinx.datetime.LocalDateTime

private val previewExampleDate = LocalDateTime(2026, 1, 15, 10, 30)

@PreviewScreenSizes
@PreviewLightDark
@Composable
private fun ImportUIJsonFileTypePreview() {
    DLLPreviewTheme {
        ImportUI(
            selectedType = FileType.JSON,
            onBrowse = {},
            onBack = {},
            onOptionSelected = {},
            exampleDate = previewExampleDate,
        )
    }
}

@PreviewScreenSizes
@PreviewLightDark
@Composable
private fun ImportUITxtFileTypePreview() {
    DLLPreviewTheme {
        ImportUI(
            selectedType = FileType.TXT,
            onBrowse = {},
            onBack = {},
            onOptionSelected = {},
            exampleDate = previewExampleDate,
        )
    }
}

@PreviewLightDark
@Composable
private fun ImportContentJsonFileTypePreview() {
    DLLPreviewTheme {
        ImportContent(
            selectedType = FileType.JSON,
            onOptionSelected = {},
            exampleDate = previewExampleDate,
        )
    }
}

@PreviewLightDark
@Composable
private fun ImportContentTxtFileTypePreview() {
    DLLPreviewTheme {
        ImportContent(
            selectedType = FileType.TXT,
            onOptionSelected = {},
            exampleDate = previewExampleDate,
        )
    }
}

@PreviewLightDark
@Composable
private fun ImportFooterPreview() {
    DLLPreviewTheme {
        ImportFooter(
            onBrowse = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun JSONTutorialPreview() {
    DLLPreviewTheme {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            JSONTutorial(
                exampleDate = previewExampleDate,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PlainTextTutorialPreview() {
    DLLPreviewTheme {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            PlainTextTutorial()
        }
    }
}
