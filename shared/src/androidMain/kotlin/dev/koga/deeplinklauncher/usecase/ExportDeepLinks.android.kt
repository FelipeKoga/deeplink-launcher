package dev.koga.deeplinklauncher.usecase

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.repository.FolderRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.Instant

actual class ExportDeepLinks(
    private val context: Context,
    private val repository: DeepLinkRepository
) {
    actual suspend fun export(type: FileType) {
        val deepLinks = repository.getAllDeepLinks().first()

        when (type) {
            FileType.JSON -> {
                val serializedData = Json.encodeToString(
                    serializer = ListSerializer(
                        DeepLink.serializer()
                    ),
                    value = deepLinks
                )
                saveToDownloads(
                    context = context,
                    fileContent = serializedData,
                    fileName = "deep_links.json",
                    type = type
                )
            }

            FileType.TXT -> {

                val serializedData = deepLinks.joinToString(separator = "\n") { deepLink ->
                    deepLink.link
                }

                saveToDownloads(
                    context = context,
                    fileContent = serializedData,
                    fileName = "deep_links.txt",
                    type = type
                )
            }
        }
    }

    private fun saveToDownloads(
        context: Context,
        fileName: String,
        fileContent: String,
        type: FileType,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore for Android 10 (API 29) and above
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, type.mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let {
                resolver.openOutputStream(it).use { outputStream ->
                    outputStream?.write(fileContent.toByteArray())
                }
            } ?: throw IOException("Failed to create new MediaStore record.")
        } else {
            // Use traditional file I/O for Android below API 29
            val downloadsPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsPath, fileName)

            FileOutputStream(file).use { outputStream ->
                outputStream.write(fileContent.toByteArray())
            }
        }

        println("Data exported to Downloads")
    }

}