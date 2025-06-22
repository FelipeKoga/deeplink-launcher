package dev.koga.deeplinklauncher.datatransfer.impl.domain.usecase

import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.ValidateDeepLink
import dev.koga.deeplinklauncher.file.GetFileContent
import dev.koga.deeplinklauncher.file.model.FileType
import dev.koga.deeplinklauncher.datatransfer.impl.domain.dto.toModel
import dev.koga.deeplinklauncher.datatransfer.domain.usecase.ImportDeepLinks
import dev.koga.deeplinklauncher.datatransfer.impl.domain.dto.Payload
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

internal class ImportDeepLinksImpl(
    private val getFileContent: GetFileContent,
    private val deepLinkRepository: DeepLinkRepository,
    private val folderRepository: FolderRepository,
    private val validateDeepLink: ValidateDeepLink,
) : ImportDeepLinks {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend operator fun invoke(
        filePath: String,
        fileType: FileType,
    ): ImportDeepLinks.Result {
        return try {
            val fileContents = getFileContent(filePath)
            when (fileType) {
                FileType.JSON -> {
                    val json = Json {
                        ignoreUnknownKeys = true
                        allowTrailingComma = true
                    }

                    val importExportDto = json.decodeFromString<Payload>(fileContents)

                    val deepLinksFromDto = importExportDto.deepLinks

                    val invalidDeepLinks = deepLinksFromDto.filter {
                        !validateDeepLink.isValid(it.link)
                    }

                    if (invalidDeepLinks.isNotEmpty()) {
                        return ImportDeepLinks.Result.Error.InvalidDeepLinksFound(
                            invalidDeepLinks.map { it.link },
                        )
                    }

                    val folders = importExportDto.folders
                        ?.map(Payload.Folder::toModel)
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
                            createdAt = newDeepLinkDto.createdAt?.let { LocalDateTime.parse(it) }
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
                        !validateDeepLink.isValid(it)
                    }

                    if (invalidDeepLinks.isNotEmpty()) {
                        return ImportDeepLinks.Result.Error.InvalidDeepLinksFound(
                            invalidDeepLinks,
                        )
                    }

                    newDeepLinksTexts
                        .map { text -> text.toDeepLink() }
                        .forEach { deepLinkRepository.upsertDeepLink(it) }
                }
            }

            ImportDeepLinks.Result.Success
        } catch (e: Exception) {
            ImportDeepLinks.Result.Error.Unknown
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    internal fun String.toDeepLink(): DeepLink {
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
}


