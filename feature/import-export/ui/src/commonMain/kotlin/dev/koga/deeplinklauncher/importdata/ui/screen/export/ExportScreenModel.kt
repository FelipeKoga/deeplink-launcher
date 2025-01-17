package dev.koga.deeplinklauncher.importdata.ui.screen.export

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.koga.deeplinklauncher.file.model.FileType
import dev.koga.deeplinklauncher.importexport.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.importexport.usecase.ExportDeepLinksResult
import dev.koga.deeplinklauncher.importexport.usecase.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.importexport.usecase.GetDeepLinksPlainTextPreview
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

    fun export(fileType: FileType) {
        screenModelScope.launch {
            when (val response = exportDeepLinks(fileType)) {
                ExportDeepLinksResult.Empty -> messageDispatcher.send(
                    "No DeepLinks to export.",
                )

                is ExportDeepLinksResult.Error -> messageDispatcher.send(
                    "An error occurred while exporting DeepLinks.",
                )

                is ExportDeepLinksResult.Success -> messageDispatcher.send(
                    "DeepLinks exported successfully. " +
                        "Check your downloads folder for a file named ${response.fileName}.",
                )
            }
        }
    }
}

data class ExportData(
    val jsonFormat: String,
    val plainTextFormat: String,
)
