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
import dev.koga.deeplinklauncher.uievent.SnackBarDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ExportViewModel(
    getDeepLinksPlainTextPreview: GetDeepLinksPlainTextPreview,
    getDeepLinksJsonPreview: GetDeepLinksJsonPreview,
    private val exportDeepLinks: ExportDeepLinks,
    private val storagePermission: StoragePermission,
    private val appNavigator: AppNavigator,
    private val snackBarDispatcher: SnackBarDispatcher,
) : ViewModel(), AppNavigator by appNavigator {

    private val plainTextPreview = getDeepLinksPlainTextPreview()
    private val jsonPreview = getDeepLinksJsonPreview()

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
                ExportDeepLinksResult.Empty -> snackBarDispatcher.show(
                    "No DeepLinks to export.",
                )

                is ExportDeepLinksResult.Error -> snackBarDispatcher.show(
                    "An error occurred while exporting DeepLinks.",
                )

                is ExportDeepLinksResult.Success -> snackBarDispatcher.show(
                    "DeepLinks exported successfully. " +
                        "Check your downloads folder for a file named ${response.fileName}.",
                )
            }
        }
    }

    fun requestPermission() {
        storagePermission.request()
    }
}

data class ExportData(
    val jsonFormat: String,
    val plainTextFormat: String,
)
