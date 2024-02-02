package dev.koga.deeplinklauncher.screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.usecase.ExportDeepLinksOutput
import dev.koga.deeplinklauncher.model.ExportFileType
import dev.koga.deeplinklauncher.usecase.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.usecase.GetDeepLinksPlainTextPreview
import dev.koga.deeplinklauncher.model.FileType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ExportScreenModel(
    getDeepLinksPlainTextPreview: GetDeepLinksPlainTextPreview,
    getDeepLinksJsonPreview: GetDeepLinksJsonPreview,
    private val exportDeepLinks: ExportDeepLinks,
) : ScreenModel {

    private val plainTextPreview = getDeepLinksPlainTextPreview()
    private val jsonPreview = getDeepLinksJsonPreview()

    private val messageDispatcher = Channel<String>()
    val messages = messageDispatcher.receiveAsFlow()

    val preview = ExportData(
        plainTextFormat = plainTextPreview,
        jsonFormat = jsonPreview,
    )

    fun export(
        exportType: ExportFileType,
    ) {
        screenModelScope.launch {
            val response = exportDeepLinks.export(
                type = when (exportType) {
                    ExportFileType.JSON -> FileType.JSON
                    ExportFileType.PLAIN_TEXT -> FileType.TXT
                },
            )

            when (response) {
                ExportDeepLinksOutput.Empty -> messageDispatcher.send(
                    "No DeepLinks to export.",
                )

                is ExportDeepLinksOutput.Error -> messageDispatcher.send(
                    "An error occurred while exporting DeepLinks.",
                )

                ExportDeepLinksOutput.Success -> messageDispatcher.send(
                    "DeepLinks exported successfully. Check your downloads folder.",
                )
            }
        }
    }
}

data class ExportData(
    val jsonFormat: String,
    val plainTextFormat: String,
)
