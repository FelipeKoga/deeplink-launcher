package dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.export

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.import.jsonStructurePreview
import dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.import.plainTextPreview
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.file.model.FileType
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun ExportUIJsonFileTypePreview() {
    DLLPreviewTheme {
        ExportUI(
            selectedExportType = FileType.JSON,
            preview = ExportData(
                jsonFormat = jsonStructurePreview,
                plainTextFormat = plainTextPreview,
            ),
            onExport = {},
            onBack = {},
            onChangeExportType = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun ExportUITTxtFileTypePreview() {
    DLLPreviewTheme {
        ExportUI(
            selectedExportType = FileType.TXT,
            preview = ExportData(
                jsonFormat = jsonStructurePreview,
                plainTextFormat = plainTextPreview,
            ),
            onExport = {},
            onBack = {},
            onChangeExportType = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun ExportContentJsonFileTypePreview() {
    DLLPreviewTheme {
        ExportContent(
            selectedExportType = FileType.JSON,
            preview = ExportData(
                jsonFormat = jsonStructurePreview,
                plainTextFormat = plainTextPreview,
            ),
            onChangeExportType = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun ExportContentTxtFileTypePreview() {
    DLLPreviewTheme {
        ExportContent(
            selectedExportType = FileType.TXT,
            preview = ExportData(
                jsonFormat = jsonStructurePreview,
                plainTextFormat = plainTextPreview,
            ),
            onChangeExportType = {},
        )
    }
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun ExportFooterPermissionGrantedPreview() {
    ExportFooter(
        isPermissionGranted = true,
        export = {},
    )
}

@Preview
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun ExportFooterPermissionNotGrantedPreview() {
    ExportFooter(
        isPermissionGranted = false,
        export = {},
    )
}
