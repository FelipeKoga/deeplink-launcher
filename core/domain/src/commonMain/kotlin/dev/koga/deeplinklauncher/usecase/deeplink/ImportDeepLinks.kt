package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.FolderDataSource
import dev.koga.deeplinklauncher.dto.ImportDto
import dev.koga.deeplinklauncher.dto.toModel
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.FileType
import dev.koga.deeplinklauncher.provider.UUIDProvider
import dev.koga.deeplinklauncher.usecase.GetFileContent
import dev.koga.deeplinklauncher.usecase.ValidateDeepLink
import kotlinx.datetime.Clock
import kotlinx.datetime.toInstant
import kotlinx.serialization.json.Json

class ImportDeepLinks(
    private val getFileContent: GetFileContent,
    private val validateDeepLink: ValidateDeepLink,
    private val deepLinkDataSource: DeepLinkDataSource,
    private val folderDataSource: FolderDataSource,
) {
    fun invoke(filePath: String, fileType: FileType): ImportDeepLinksOutput {
        return try {
            val fileContents = getFileContent(filePath)

            when (fileType) {
                FileType.JSON -> {

                    val importDto = Json.decodeFromString<ImportDto>(fileContents)

                    val deepLinksFromDto = importDto.deepLinks

                    val invalidDeepLinks = deepLinksFromDto.filter {
                        !validateDeepLink(it.link)
                    }

                    if (invalidDeepLinks.isNotEmpty()) {
                        return ImportDeepLinksOutput.Error.InvalidDeepLinksFound(
                            invalidDeepLinks.map { it.link },
                        )
                    }

                    val folders = importDto.folders.map(ImportDto.Folder::toModel)

                    folders.forEach {
                        folderDataSource.upsertFolder(it)
                    }

                    val databaseDeepLinks = deepLinksFromDto.mapNotNull {
                        deepLinkDataSource.getDeepLinkByLink(it.link)
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
                            createdAt = newDeepLinkDto.createdAt?.toInstant()
                                ?: databaseDeepLink.createdAt,
                            folder = folders.find { folder -> folder.id == newDeepLinkDto.folderId }
                                ?: databaseDeepLink.folder,
                        )
                    }

                    (newDeepLinks + updatedDeepLinks).forEach {
                        deepLinkDataSource.upsertDeepLink(it)
                    }
                }

                FileType.TXT -> {
                    val deepLinksTexts = fileContents.split("\n")

                    val databaseDeepLinks = deepLinksTexts.mapNotNull {
                        deepLinkDataSource.getDeepLinkByLink(it)
                    }

                    val newDeepLinksTexts = deepLinksTexts.filter {
                        databaseDeepLinks.none { databaseDeepLink -> databaseDeepLink.link == it }
                    }

                    val invalidDeepLinks = newDeepLinksTexts.filter {
                        !validateDeepLink(it)
                    }

                    if (invalidDeepLinks.isNotEmpty()) {
                        return ImportDeepLinksOutput.Error.InvalidDeepLinksFound(
                            invalidDeepLinks,
                        )
                    }

                    newDeepLinksTexts.map(String::toDeepLink).forEach {
                        deepLinkDataSource.upsertDeepLink(it)
                    }
                }
            }

            ImportDeepLinksOutput.Success
        } catch (e: Exception) {
            ImportDeepLinksOutput.Error.Unknown
        }
    }
}

interface ImportDeepLinksOutput {
    data object Success : ImportDeepLinksOutput

    sealed interface Error : ImportDeepLinksOutput {
        data class InvalidDeepLinksFound(val invalidDeepLinks: List<String>) : Error
        data object Unknown : Error
    }
}

fun String.toDeepLink(): DeepLink {
    return DeepLink(
        id = UUIDProvider.get(),
        createdAt = Clock.System.now(),
        link = this,
        name = null,
        description = null,
        isFavorite = false,
        folder = null,
    )
}
