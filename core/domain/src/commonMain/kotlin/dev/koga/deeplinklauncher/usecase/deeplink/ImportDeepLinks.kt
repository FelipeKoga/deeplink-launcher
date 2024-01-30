package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.dto.ImportExportDeepLinkDto
import dev.koga.deeplinklauncher.dto.toDeepLink
import dev.koga.deeplinklauncher.dto.toFolder
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
    private val dataSource: DeepLinkDataSource,
) {
    fun invoke(filePath: String, fileType: FileType): ImportDeepLinksOutput {
        return try {
            val fileContents = getFileContent(filePath)

            when (fileType) {
                FileType.JSON -> {
                    val deepLinksFromJson =
                        Json.decodeFromString<List<ImportExportDeepLinkDto>>(fileContents)

                    val invalidDeepLinks = deepLinksFromJson.filter {
                        !validateDeepLink(it.link)
                    }

                    if (invalidDeepLinks.isNotEmpty()) {
                        return ImportDeepLinksOutput.Error.InvalidDeepLinksFound(
                            invalidDeepLinks.map { it.link },
                        )
                    }

                    val databaseDeepLinks = deepLinksFromJson.mapNotNull {
                        dataSource.getDeepLinkByLink(it.link)
                    }

                    val newDeepLinks = deepLinksFromJson.filter {
                        databaseDeepLinks.none { databaseDeepLink -> databaseDeepLink.link == it.link }
                    }.map(ImportExportDeepLinkDto::toDeepLink)

                    val updatedDeepLinks = deepLinksFromJson.mapNotNull { newDeepLinkDto ->
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
                            folder = newDeepLinkDto.folder?.toFolder() ?: databaseDeepLink.folder,
                        )
                    }

                    (newDeepLinks + updatedDeepLinks).forEach {
                        dataSource.upsertDeepLink(it)
                    }
                }

                FileType.TXT -> {
                    val deepLinksTexts = fileContents.split("\n")

                    val databaseDeepLinks = deepLinksTexts.mapNotNull {
                        dataSource.getDeepLinkByLink(it)
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
                        dataSource.upsertDeepLink(it)
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
