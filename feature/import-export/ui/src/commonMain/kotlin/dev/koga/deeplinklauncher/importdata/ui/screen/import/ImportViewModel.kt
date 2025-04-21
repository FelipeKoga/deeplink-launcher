package dev.koga.deeplinklauncher.importdata.ui.screen.import

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import dev.koga.deeplinklauncher.file.GetFileRealPath
import dev.koga.deeplinklauncher.file.model.FileType
import dev.koga.deeplinklauncher.importexport.usecase.ImportDeepLinks
import dev.koga.deeplinklauncher.importexport.usecase.ImportDeepLinksResult
import dev.koga.deeplinklauncher.navigation.AppNavigator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ImportViewModel(
    private val importDeepLinks: ImportDeepLinks,
    private val getFileRealPath: GetFileRealPath,
    private val appNavigator: AppNavigator,
) : ViewModel(), AppNavigator by appNavigator {

    private val _messageDispatcher = Channel<String>(Channel.UNLIMITED)
    val messages = _messageDispatcher.receiveAsFlow()

    fun import(platformFile: MPFile<Any>) = viewModelScope.launch {
        val path = getFileRealPath.get(platformFile.path)

        val fileType = when (path.substringAfterLast(".")) {
            FileType.TXT.extension -> FileType.TXT
            FileType.JSON.extension -> FileType.JSON
            else -> {
                _messageDispatcher.send("Unsupported file type")
                return@launch
            }
        }

        val response = importDeepLinks(
            filePath = path,
            fileType = fileType,
        )

        when (response) {
            is ImportDeepLinksResult.Success -> {
                _messageDispatcher.send("DeepLinks imported successfully")
            }

            is ImportDeepLinksResult.Error -> {
                _messageDispatcher.send(
                    "Something went wrong. " +
                            "Check the content structure and try again.",
                )
            }
        }
    }
}
