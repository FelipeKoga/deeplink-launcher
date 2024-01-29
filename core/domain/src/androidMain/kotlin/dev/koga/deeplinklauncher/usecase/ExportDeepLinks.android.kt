package dev.koga.deeplinklauncher.usecase

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import dev.koga.deeplinklauncher.constant.defaultDeepLink
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.dto.ImportDeepLinkDto
import dev.koga.deeplinklauncher.dto.ImportFolderDto
import dev.koga.deeplinklauncher.model.FileType
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

actual class ExportDeepLinks(
    private val context: Context,
    private val dataSource: DeepLinkDataSource
) {
    actual suspend fun export(type: FileType): ExportDeepLinksOutput {
        val deepLinks = dataSource.getDeepLinks().filter { it != defaultDeepLink }.ifEmpty {
            return ExportDeepLinksOutput.Empty
        }

        return try {
            val data = when (type) {
                FileType.JSON -> Json.encodeToString(
                    serializer = ListSerializer(
                        ImportDeepLinkDto.serializer()
                    ),
                    value = deepLinks.map {
                        ImportDeepLinkDto(
                            id = it.id,
                            createdAt = it.createdAt.toString(),
                            link = it.link,
                            name = it.name,
                            description = it.description,
                            folder = it.folder?.let { folder ->
                                ImportFolderDto(
                                    id = folder.id,
                                    name = folder.name,
                                    description = folder.description
                                )
                            },
                            isFavorite = it.isFavorite
                        )
                    }
                )


                FileType.TXT -> deepLinks.joinToString(separator = "\n") { deepLink ->
                    deepLink.link
                }

            }

            val fileName = "deeplinks-${
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }.${type.extension}"

            val uri = saveToDownloads(
                context = context,
                fileContent = data,
                fileName = fileName,
                type = type
            )

            shareFile(
                uri = uri,
                fileType = type
            )

            ExportDeepLinksOutput.Success
        } catch (e: Exception) {
            ExportDeepLinksOutput.Error(e)
        }
    }

    private fun saveToDownloads(
        context: Context,
        fileName: String,
        fileContent: String,
        type: FileType,
    ): Uri {
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

            uri
        } else {
            val downloadsPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsPath, fileName)

            FileOutputStream(file).use { outputStream ->
                outputStream.write(fileContent.toByteArray())
            }

            // Use FileProvider to get content URI
            FileProvider.getUriForFile(context, context.packageName + ".provider", file)
        }
    }

    private fun shareFile(uri: Uri, fileType: FileType) {
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