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
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@ViewModelKey
@ContributesIntoMap(AppScope::class, binding<ViewModel>())
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

    private val permissionRequestChannel = Channel<FileType>(Channel.CONFLATED)
    val permissionRequests: Flow<FileType> = permissionRequestChannel.receiveAsFlow()

    fun export(fileType: FileType) {
        if (!storagePermission.isGranted()) {
            permissionRequestChannel.trySend(fileType)
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

    fun onPermissionResult(fileType: FileType, granted: Boolean) {
        if (granted) {
            export(fileType)
        } else {
            snackBarDispatcher.show(
                "Allow storage access to save the export in your downloads folder. " +
                    "If you chose not to be asked again, enable it in the app settings.",
            )
        }
    }
}

data class ExportData(
    val jsonFormat: String,
    val plainTextFormat: String,
)
