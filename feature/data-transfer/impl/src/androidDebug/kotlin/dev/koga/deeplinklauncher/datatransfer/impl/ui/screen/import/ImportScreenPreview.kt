package dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.import

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.file.model.FileType
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun ImportUIJsonFileTypePreview() {
    DLLPreviewTheme {
        ImportUI(
            selectedType = FileType.JSON,
            onBrowse = {},
            onBack = {},
            onOptionSelected = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun ImportUITxtFileTypePreview() {
    DLLPreviewTheme {
        ImportUI(
            selectedType = FileType.TXT,
            onBrowse = {},
            onBack = {},
            onOptionSelected = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun ImportContentJsonFileTypePreview() {
    DLLPreviewTheme {
        ImportContent(
            selectedType = FileType.JSON,
            onOptionSelected = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun ImportContentTxtFileTypePreview() {
    DLLPreviewTheme {
        ImportContent(
            selectedType = FileType.TXT,
            onOptionSelected = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun ImportFooterPreview() {
    DLLPreviewTheme {
        ImportFooter(
            onBrowse = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun JSONTutorialPreview() {
    DLLPreviewTheme {
        JSONTutorial()
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun PlainTextTutorialPreview() {
    DLLPreviewTheme {
        PlainTextTutorial()
    }
}
