package dev.koga.deeplinklauncher.importdata.ui.screen.import

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import dev.koga.deeplinklauncher.file.GetFileRealPath
import dev.koga.deeplinklauncher.file.model.FileType
import dev.koga.deeplinklauncher.importexport.usecase.ImportDeepLinks
import dev.koga.deeplinklauncher.importexport.usecase.ImportDeepLinksResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ImportScreenModel(
    private val importDeepLinks: ImportDeepLinks,
    private val getFileRealPath: GetFileRealPath,
) : ScreenModel {

    private val _messageDispatcher = Channel<String>(Channel.UNLIMITED)
    val messages = _messageDispatcher.receiveAsFlow()

    fun import(platformFile: MPFile<Any>) = screenModelScope.launch {
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
