package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.dto.ImportExportDto
import dev.koga.deeplinklauncher.dto.dateFormat
import dev.koga.deeplinklauncher.model.FileType
import dev.koga.deeplinklauncher.usecase.SaveFile
import dev.koga.deeplinklauncher.usecase.ShareFile
import dev.koga.deeplinklauncher.util.constant.defaultDeepLink
import dev.koga.deeplinklauncher.util.currentLocalDateTime
import dev.koga.deeplinklauncher.util.ext.format
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class ExportDeepLinks(
    private val dataSource: DeepLinkDataSource,
    private val saveFile: SaveFile,
    private val shareFile: ShareFile,
) {
    fun export(type: FileType): ExportDeepLinksOutput {
        val deepLinks = dataSource.getDeepLinks().filter { it.id != defaultDeepLink.id }.ifEmpty {
            return ExportDeepLinksOutput.Empty
        }

        val serializedData = when (type) {
            FileType.JSON -> {
                val foldersDto = deepLinks
                    .mapNotNull { it.folder }
                    .map {
                        ImportExportDto.Folder(
                            id = it.id,
                            name = it.name,
                            description = it.description,
                        )
                    }.distinct()

                val deepLinksDto = deepLinks.map {
                    ImportExportDto.DeepLink(
                        link = it.link,
                        id = it.id,
                        createdAt = it.createdAt.format(dateFormat),
                        name = it.name,
                        description = it.description,
                        folderId = it.folder?.id,
                        isFavorite = it.isFavorite,
                    )
                }

                Json.encodeToString(
                    serializer = ListSerializer(
                        ImportExportDto.serializer(),
                    ),

                    value = deepLinks.map {
                        ImportExportDto(
                            folders = foldersDto,
                            deepLinks = deepLinksDto,
                        )
                    },
                )
            }

            FileType.TXT -> deepLinks.joinToString(separator = "\n") { deepLink ->
                deepLink.link
            }
        }

        val fileName = "deeplinks-${currentLocalDateTime}.${type.extension}"

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
