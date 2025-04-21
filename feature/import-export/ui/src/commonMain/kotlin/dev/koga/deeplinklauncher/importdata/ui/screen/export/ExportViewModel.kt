package dev.koga.deeplinklauncher.importdata.ui.screen.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.file.StoragePermission
import dev.koga.deeplinklauncher.file.model.FileType
import dev.koga.deeplinklauncher.importexport.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.importexport.usecase.ExportDeepLinksResult
import dev.koga.deeplinklauncher.importexport.usecase.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.importexport.usecase.GetDeepLinksPlainTextPreview
import dev.koga.deeplinklauncher.navigation.AppNavigator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ExportViewModel(
    getDeepLinksPlainTextPreview: GetDeepLinksPlainTextPreview,
    getDeepLinksJsonPreview: GetDeepLinksJsonPreview,
    private val exportDeepLinks: ExportDeepLinks,
    private val storagePermission: StoragePermission,
    private val appNavigator: AppNavigator,
) : ViewModel(), AppNavigator by appNavigator {

    private val plainTextPreview = getDeepLinksPlainTextPreview()
    private val jsonPreview = getDeepLinksJsonPreview()

    private val messageDispatcher = Channel<String>()
    val messages = messageDispatcher.receiveAsFlow()

    val preview = ExportData(
        plainTextFormat = plainTextPreview,
        jsonFormat = jsonPreview,
    )

    fun export(fileType: FileType) {
        if (!storagePermission.isGranted()) {
            storagePermission.request()
            return
        }

        viewModelScope.launch {
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
