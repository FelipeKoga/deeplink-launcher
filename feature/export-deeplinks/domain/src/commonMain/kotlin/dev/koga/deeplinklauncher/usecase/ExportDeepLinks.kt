package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.dto.ImportExportDto
import dev.koga.deeplinklauncher.dto.dateFormat
import dev.koga.deeplinklauncher.model.FileType
import dev.koga.deeplinklauncher.util.ext.currentLocalDateTime
import dev.koga.deeplinklauncher.util.ext.format
import kotlinx.serialization.json.Json

class ExportDeepLinks(
    private val dataSource: DeepLinkDataSource,
    private val saveFile: SaveFile,
    private val shareFile: ShareFile,
) {
    fun export(type: FileType): ExportDeepLinksOutput {
        val deepLinks = dataSource.getDeepLinks().ifEmpty {
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
                    serializer = ImportExportDto.serializer(),
                    value = ImportExportDto(
                        folders = foldersDto,
                        deepLinks = deepLinksDto,
                    ),
                )
            }

            FileType.TXT -> deepLinks.joinToString(separator = "\n") { deepLink ->
                deepLink.link
            }
        }

        val fileName = "deeplinks-$currentLocalDateTime.${type.extension}"

        val filePath = saveFile(
            fileName = fileName,
            fileContent = serializedData,
            type = type,
        ) ?: return ExportDeepLinksOutput.Error(Exception("Failed to save file"))

        shareFile(
            filePath = filePath,
            fileType = type,
        )

        return ExportDeepLinksOutput.Success(fileName = fileName)
    }
}

sealed interface ExportDeepLinksOutput {
    data class Success(val fileName: String) : ExportDeepLinksOutput
    data object Empty : ExportDeepLinksOutput
    data class Error(val throwable: Throwable) : ExportDeepLinksOutput
}
