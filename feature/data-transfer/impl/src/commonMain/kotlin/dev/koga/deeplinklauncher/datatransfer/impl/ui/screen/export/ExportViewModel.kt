package dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.datatransfer.api.domain.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.datatransfer.api.domain.usecase.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.datatransfer.api.domain.usecase.GetDeepLinksPlainTextPreview
import dev.koga.deeplinklauncher.datatransfer.impl.analytics.DataExported
import dev.koga.deeplinklauncher.datatransfer.impl.analytics.track
import dev.koga.deeplinklauncher.file.StoragePermission
import dev.koga.deeplinklauncher.file.model.FileType
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.uievent.SnackBarDispatcher
import kotlinx.coroutines.launch

class ExportViewModel(
    getDeepLinksPlainTextPreview: GetDeepLinksPlainTextPreview,
    getDeepLinksJsonPreview: GetDeepLinksJsonPreview,
    private val exportDeepLinks: ExportDeepLinks,
    private val storagePermission: StoragePermission,
    private val appNavigator: AppNavigator,
    private val snackBarDispatcher: SnackBarDispatcher,
    private val analyticsTracker: AnalyticsTracker,
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
                ExportDeepLinks.Result.Empty -> snackBarDispatcher.show(
                    "No DeepLinks to export.",
                )

                is ExportDeepLinks.Result.Error -> snackBarDispatcher.show(
                    "An error occurred while exporting DeepLinks.",
                )

                is ExportDeepLinks.Result.Success -> {
                    analyticsTracker.track(DataExported)
                    snackBarDispatcher.show(
                        "DeepLinks exported successfully. " +
                            "Check your downloads folder for a file named ${response.fileName}.",
                    )
                }
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
