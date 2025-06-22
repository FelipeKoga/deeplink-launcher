package dev.koga.deeplinklauncher.datatransfer.impl.domain.usecase

import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.date.format
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.file.SaveFile
import dev.koga.deeplinklauncher.file.ShareFile
import dev.koga.deeplinklauncher.file.model.FileType
import dev.koga.deeplinklauncher.datatransfer.impl.domain.dto.dateFormat
import dev.koga.deeplinklauncher.datatransfer.domain.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.datatransfer.impl.domain.dto.Payload
import dev.koga.deeplinklauncher.platform.canShareContent
import kotlinx.serialization.json.Json

internal class ExportDeepLinksImpl(
    private val repository: DeepLinkRepository,
    private val saveFile: SaveFile,
    private val shareFile: ShareFile,
) : ExportDeepLinks {
    override fun invoke(type: FileType): ExportDeepLinks.Result {
        val deepLinks = repository.getDeepLinks().ifEmpty {
            return ExportDeepLinks.Result.Empty
        }

        val serializedData = when (type) {
            FileType.JSON -> {
                val foldersDto = deepLinks
                    .mapNotNull { it.folder }
                    .map {
                        Payload.Folder(
                            id = it.id,
                            name = it.name,
                            description = it.description,
                        )
                    }.distinct()

                val deepLinksDto = deepLinks.map {
                    Payload.DeepLink(
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
                    serializer = Payload.serializer(),
                    value = Payload(
                        folders = foldersDto,
                        deepLinks = deepLinksDto,
                    ),
                )
            }

            FileType.TXT -> deepLinks.joinToString(separator = "\n") { deepLink ->
                deepLink.link
            }
        }

        val sanitizedTimestamp = currentLocalDateTime.toString().replace(':', '_')
        val fileName = "deeplinks-$sanitizedTimestamp.${type.extension}"

        val filePath = saveFile(
            fileName = fileName,
            fileContent = serializedData,
            type = type,
        ) ?: return ExportDeepLinks.Result.Error(Exception("Failed to save file"))

        if (canShareContent) {
            shareFile(
                filePath = filePath,
                fileType = type,
            )
        }

        return ExportDeepLinks.Result.Success(fileName = fileName)
    }
}
