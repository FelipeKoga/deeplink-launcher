package dev.koga.deeplinklauncher.importexport.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.isLinkValid
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.file.GetFileContent
import dev.koga.deeplinklauncher.importexport.dto.ImportExportDto
import dev.koga.deeplinklauncher.importexport.dto.toModel
import dev.koga.deeplinklauncher.importexport.model.FileType
import dev.koga.deeplinklauncher.importexport.usecase.ImportDeepLinks
import dev.koga.deeplinklauncher.importexport.usecase.ImportDeepLinksResult
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class DefaultImportDeepLinks(
    private val getFileContent: GetFileContent,
    private val deepLinkRepository: DeepLinkRepository,
    private val folderRepository: FolderRepository,
) : ImportDeepLinks {

    @OptIn(ExperimentalSerializationApi::class)
    override operator fun invoke(
        filePath: String,
        fileType: FileType,
    ): ImportDeepLinksResult {
        return try {
            val fileContents = getFileContent(filePath)
            when (fileType) {
                FileType.JSON -> {
                    val json = Json {
                        ignoreUnknownKeys = true
                        allowTrailingComma = true
                    }

                    val importExportDto = json.decodeFromString<ImportExportDto>(fileContents)

                    val deepLinksFromDto = importExportDto.deepLinks

                    val invalidDeepLinks = deepLinksFromDto.filter {
                        !it.link.isLinkValid
                    }

                    if (invalidDeepLinks.isNotEmpty()) {
                        return ImportDeepLinksResult.Error.InvalidDeepLinksFound(
                            invalidDeepLinks.map { it.link },
                        )
                    }

                    val folders = importExportDto.folders
                        ?.map(ImportExportDto.Folder::toModel)
                        ?: emptyList()

                    folders.forEach {
                        folderRepository.upsertFolder(it)
                    }

                    val databaseDeepLinks = deepLinksFromDto.mapNotNull {
                        deepLinkRepository.getDeepLinkByLink(it.link)
                    }

                    val newDeepLinks = deepLinksFromDto.filter {
                        databaseDeepLinks.none { databaseDeepLink -> databaseDeepLink.link == it.link }
                    }.map {
                        it.toModel(folders.find { folder -> folder.id == it.folderId })
                    }

                    val updatedDeepLinks = deepLinksFromDto.mapNotNull { newDeepLinkDto ->
                        val databaseDeepLink = databaseDeepLinks.find { databaseDeepLink ->
                            databaseDeepLink.link == newDeepLinkDto.link
                        } ?: return@mapNotNull null

                        databaseDeepLink.copy(
                            id = databaseDeepLink.id,
                            name = newDeepLinkDto.name ?: databaseDeepLink.name,
                            description = newDeepLinkDto.description
                                ?: databaseDeepLink.description,
                            isFavorite = newDeepLinkDto.isFavorite ?: databaseDeepLink.isFavorite,
                            createdAt = newDeepLinkDto.createdAt?.toLocalDateTime()
                                ?: databaseDeepLink.createdAt,
                            folder = folders.find { folder -> folder.id == newDeepLinkDto.folderId }
                                ?: databaseDeepLink.folder,
                        )
                    }

                    (newDeepLinks + updatedDeepLinks).forEach {
                        deepLinkRepository.upsertDeepLink(it)
                    }
                }

                FileType.TXT -> {
                    val deepLinksTexts = fileContents.split("\n")

                    val databaseDeepLinks = deepLinksTexts.mapNotNull {
                        deepLinkRepository.getDeepLinkByLink(it)
                    }

                    val newDeepLinksTexts = deepLinksTexts.filter {
                        databaseDeepLinks.none { databaseDeepLink -> databaseDeepLink.link == it }
                    }

                    val invalidDeepLinks = newDeepLinksTexts.filter {
                        !it.isLinkValid
                    }

                    if (invalidDeepLinks.isNotEmpty()) {
                        return ImportDeepLinksResult.Error.InvalidDeepLinksFound(
                            invalidDeepLinks,
                        )
                    }

                    newDeepLinksTexts.map(String::toDeepLink).forEach {
                        deepLinkRepository.upsertDeepLink(it)
                    }
                }
            }

            ImportDeepLinksResult.Success
        } catch (e: Exception) {
            ImportDeepLinksResult.Error.Unknown
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
fun String.toDeepLink(): DeepLink {
    return DeepLink(
        id = Uuid.random().toString(),
        createdAt = currentLocalDateTime,
        link = this,
        name = null,
        description = null,
        isFavorite = false,
        folder = null,
    )
}
