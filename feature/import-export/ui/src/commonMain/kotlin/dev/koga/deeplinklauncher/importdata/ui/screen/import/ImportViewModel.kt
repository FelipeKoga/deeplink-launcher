package dev.koga.deeplinklauncher.importdata.ui.screen.import

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import dev.koga.deeplinklauncher.file.GetFileRealPath
import dev.koga.deeplinklauncher.file.model.FileType
import dev.koga.deeplinklauncher.importexport.usecase.ImportDeepLinks
import dev.koga.deeplinklauncher.importexport.usecase.ImportDeepLinksResult
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.uievent.SnackBarDispatcher
import kotlinx.coroutines.launch

class ImportViewModel(
    private val importDeepLinks: ImportDeepLinks,
    private val getFileRealPath: GetFileRealPath,
    private val appNavigator: AppNavigator,
    private val snackBarDispatcher: SnackBarDispatcher,
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
            is ImportDeepLinksResult.Success -> {
                snackBarDispatcher.show("DeepLinks imported successfully")
            }

            is ImportDeepLinksResult.Error -> {
                snackBarDispatcher.show(
                    "Something went wrong. " +
                        "Check the content structure and try again.",
                )
            }
        }
    }
}
