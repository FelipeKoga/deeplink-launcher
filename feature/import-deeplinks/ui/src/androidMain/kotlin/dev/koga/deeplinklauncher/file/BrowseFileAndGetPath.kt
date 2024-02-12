package dev.koga.deeplinklauncher.file

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import dev.koga.deeplinklauncher.model.FileType
import dev.koga.deeplinklauncher.util.ext.getRealPathFromUri

actual class BrowseFileAndGetPath(
    private val context: Context,
) {

    private var filePickerLauncher: ActivityResultLauncher<String>? = null

    @Composable
    actual fun Listen(onResult: (realPath: String, fileType: FileType?) -> Unit) {
        filePickerLauncher = rememberLauncherForActivityResult(
            contract = object : ActivityResultContracts.GetContent() {
                override fun createIntent(context: Context, input: String): Intent {
                    return super.createIntent(context, input)
                        .putExtra(
                            Intent.EXTRA_MIME_TYPES,
                            arrayOf(
                                FileType.TXT.mimeType,
                                FileType.JSON.mimeType,
                                "application/*",
                            )
                        )
                }
            },
            onResult = { uri ->
                val filePath = uri?.getRealPathFromUri(context).orEmpty()

                val fileType = when (filePath.substringAfterLast(".")) {
                    FileType.TXT.extension -> FileType.TXT
                    FileType.JSON.extension -> FileType.JSON
                    else -> null
                }

                onResult(filePath, fileType)
            },
        )
    }

    actual fun launch() {
        filePickerLauncher?.launch("*/*")
    }
}