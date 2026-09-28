package dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.export

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.import.jsonStructurePreview
import dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.import.plainTextPreview
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import dev.koga.deeplinklauncher.file.model.FileType

@PreviewScreenSizes
@PreviewLightDark
@Composable
private fun ExportUIJsonFileTypePreview() {
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

@PreviewScreenSizes
@PreviewLightDark
@Composable
private fun ExportUITxtFileTypePreview() {
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

@PreviewLightDark
@Composable
private fun ExportContentJsonFileTypePreview() {
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

@PreviewLightDark
@Composable
private fun ExportContentTxtFileTypePreview() {
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

@PreviewLightDark
@Composable
private fun ExportFooterPermissionGrantedPreview() {
    DLLPreviewTheme {
        ExportFooter(
            isPermissionGranted = true,
            export = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun ExportFooterPermissionNotGrantedPreview() {
    DLLPreviewTheme {
        ExportFooter(
            isPermissionGranted = false,
            export = {},
        )
    }
}
