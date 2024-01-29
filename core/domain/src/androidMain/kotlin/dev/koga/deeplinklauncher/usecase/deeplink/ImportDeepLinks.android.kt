package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.dto.ImportDeepLinkDto
import dev.koga.deeplinklauncher.model.FileType
import dev.koga.deeplinklauncher.util.isUriValid
import kotlinx.datetime.toInstant
import kotlinx.serialization.json.Json
import java.io.File

actual class ImportDeepLinks(
    private val dataSource: DeepLinkDataSource
) {

    actual fun invoke(filePath: String, fileType: FileType): ImportDeepLinksOutput {
        return try {
            val fileContents = File(filePath).readText()

            when (fileType) {
                FileType.JSON -> {
                    val deepLinksFromJson =
                        Json.decodeFromString<List<ImportDeepLinkDto>>(fileContents)

                    val invalidDeepLinks = deepLinksFromJson.filter {
                        !it.link.isUriValid()
                    }

                    if (invalidDeepLinks.isNotEmpty()) {
                        return ImportDeepLinksOutput.Error.InvalidDeepLinksFound(
                            invalidDeepLinks.map { it.link }
                        )
                    }

                    val databaseDeepLinks = deepLinksFromJson.mapNotNull {
                        dataSource.getDeepLinkByLink(it.link)
                    }

                    val newDeepLinks = deepLinksFromJson.filter {
                        databaseDeepLinks.none { databaseDeepLink -> databaseDeepLink.link == it.link }
                    }.map(ImportDeepLinkDto::toDeepLink)

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
                            folder = newDeepLinkDto.folder?.toFolder() ?: databaseDeepLink.folder
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
                        !it.isUriValid()
                    }

                    if (invalidDeepLinks.isNotEmpty()) {
                        return ImportDeepLinksOutput.Error.InvalidDeepLinksFound(
                            invalidDeepLinks
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