package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.FolderDataSource
import dev.koga.deeplinklauncher.dto.ImportExportDto
import dev.koga.deeplinklauncher.dto.toModel
import dev.koga.deeplinklauncher.model.FileType
import dev.koga.deeplinklauncher.model.isLinkValid
import dev.koga.deeplinklauncher.platform.GetFileContent
import dev.koga.deeplinklauncher.util.ext.toDeepLink
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

class ImportDeepLinks(
    private val getFileContent: GetFileContent,
    private val deepLinkDataSource: DeepLinkDataSource,
    private val folderDataSource: FolderDataSource,
) {
    @OptIn(ExperimentalSerializationApi::class)
    fun invoke(filePath: String, fileType: FileType): ImportDeepLinksOutput {
        return try {
            val fileContents = getFileContent(filePath)

            println(fileContents)

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
                        return ImportDeepLinksOutput.Error.InvalidDeepLinksFound(
                            invalidDeepLinks.map { it.link },
                        )
                    }

                    val folders = importExportDto.folders
                        ?.map(ImportExportDto.Folder::toModel)
                        ?: emptyList()

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
                            createdAt = newDeepLinkDto.createdAt?.toLocalDateTime()
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
                        !it.isLinkValid
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
            println(e)
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
