package dev.koga.deeplinklauncher.usecase

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import dev.koga.deeplinklauncher.model.FileType
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

actual class SaveFile(
    private val context: Context,
) {
    actual operator fun invoke(
        fileName: String,
        fileContent: String,
        type: FileType,
    ): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, type.mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                ?: throw IOException("Failed to create new MediaStore record.")

            resolver.openOutputStream(uri).use { outputStream ->
                outputStream?.write(fileContent.toByteArray())
            }

            uri.toString()
        } else {
            val downloadsPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            val file = File(downloadsPath, fileName)

            FileOutputStream(file).use { outputStream ->
                outputStream.write(fileContent.toByteArray())
            }

            file.absolutePath
        }
    }
}
