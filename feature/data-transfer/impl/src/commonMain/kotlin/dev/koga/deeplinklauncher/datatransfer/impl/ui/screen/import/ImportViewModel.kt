package dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.import

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.datatransfer.api.domain.usecase.ImportDeepLinks
import dev.koga.deeplinklauncher.datatransfer.impl.analytics.DataImported
import dev.koga.deeplinklauncher.datatransfer.impl.analytics.track
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.file.GetFileRealPath
import dev.koga.deeplinklauncher.file.model.FileType
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.uievent.SnackBarDispatcher
import kotlinx.coroutines.launch

class ImportViewModel(
    private val importDeepLinks: ImportDeepLinks,
    private val getFileRealPath: GetFileRealPath,
    private val deepLinkRepository: DeepLinkRepository,
    private val folderRepository: FolderRepository,
    private val appNavigator: AppNavigator,
    private val snackBarDispatcher: SnackBarDispatcher,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel(), AppNavigator by appNavigator {

    fun import(platformFile: MPFile<Any>) = viewModelScope.launch {
        val path = getFileRealPath.get(platformFile.path)

        val fileType = when (path.substringAfterLast(".")) {
            FileType.TXT.extension -> FileType.TXT
            FileType.JSON.extension -> FileType.JSON
            else -> {
                snackBarDispatcher.show("Unsupported file type")
                return@launch
            }
        }

        val response = importDeepLinks(
            filePath = path,
            fileType = fileType,
        )

        when (response) {
            is ImportDeepLinks.Result.Success -> {
                analyticsTracker.track(
                    DataImported(
                        deeplinkCount = deepLinkRepository.getDeepLinks().size,
                        folderCount = folderRepository.getFolders().size,
                    ),
                )
                snackBarDispatcher.show("DeepLinks imported successfully")
            }

            is ImportDeepLinks.Result.Error -> {
                snackBarDispatcher.show(
                    "Something went wrong. " +
                        "Check the content structure and try again.",
                )
            }
        }
    }
}
