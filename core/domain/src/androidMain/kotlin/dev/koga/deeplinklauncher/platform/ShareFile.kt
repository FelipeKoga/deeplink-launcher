package dev.koga.deeplinklauncher.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import dev.koga.deeplinklauncher.model.FileType
import java.io.File

actual class ShareFile(
    private val context: Context,
) {
    actual operator fun invoke(filePath: String, fileType: FileType) {
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Uri.parse(filePath) // Parse the content URI string
        } else {
            val file = File(filePath)
            FileProvider.getUriForFile(
                context,
                "dev.koga.deeplinklauncher.android.provider",
                file,
            )
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = fileType.mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(intent, "Exported DeepLinks")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(chooser)
    }
}
