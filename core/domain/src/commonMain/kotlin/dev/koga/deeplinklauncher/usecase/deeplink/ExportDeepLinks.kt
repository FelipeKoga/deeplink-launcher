package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.constant.defaultDeepLink
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.dto.ImportExportDeepLinkDto
import dev.koga.deeplinklauncher.dto.ImportExportFolderDto
import dev.koga.deeplinklauncher.model.FileType
import dev.koga.deeplinklauncher.usecase.SaveFile
import dev.koga.deeplinklauncher.usecase.ShareFile
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class ExportDeepLinks(
    private val dataSource: DeepLinkDataSource,
    private val saveFile: SaveFile,
    private val shareFile: ShareFile,
) {
    fun export(type: FileType): ExportDeepLinksOutput {
        val deepLinks = dataSource.getDeepLinks().filter { it != defaultDeepLink }.ifEmpty {
            return ExportDeepLinksOutput.Empty
        }

        val serializedData = when (type) {
            FileType.JSON -> Json.encodeToString(
                serializer = ListSerializer(
                    ImportExportDeepLinkDto.serializer(),
                ),

                value = deepLinks.map {
                    ImportExportDeepLinkDto(
                        id = it.id,
                        createdAt = it.createdAt.toString(),
                        link = it.link,
                        name = it.name,
                        description = it.description,
                        folder = it.folder?.let { folder ->
                            ImportExportFolderDto(
                                id = folder.id,
                                name = folder.name,
                                description = folder.description,
                            )
                        },
                        isFavorite = it.isFavorite,
                    )
                },
            )

            FileType.TXT -> deepLinks.joinToString(separator = "\n") { deepLink ->
                deepLink.link
            }
        }

        val fileName = "deeplinks-${
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        }.${type.extension}"

        val filePath = saveFile(
            fileName = fileName,
            fileContent = serializedData,
            type = type,
        ) ?: return ExportDeepLinksOutput.Error(Exception("Failed to save file"))

        shareFile(
            filePath = filePath,
            fileType = type,
        )

        return ExportDeepLinksOutput.Success
    }
}

sealed interface ExportDeepLinksOutput {
    data object Success : ExportDeepLinksOutput
    data object Empty : ExportDeepLinksOutput
    data class Error(val throwable: Throwable) : ExportDeepLinksOutput
}
