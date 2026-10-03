package dev.koga.deeplinklauncher.datatransfer.impl.domain.usecase

import dev.koga.deeplinklauncher.datatransfer.api.domain.usecase.ImportDeepLinks
import dev.koga.deeplinklauncher.datatransfer.impl.data.dto.Payload
import dev.koga.deeplinklauncher.datatransfer.impl.data.dto.toModel
import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ValidateDeepLink
import dev.koga.deeplinklauncher.file.GetFileContent
import dev.koga.deeplinklauncher.file.model.FileType
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
    private val shortcutManager: DeepLinkShortcutManager,
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

                    val folders = importFolders(
                        importExportDto.folders.orEmpty().map(Payload.Folder::toModel),
                    )

                    val createdIds = deepLinksFromDto
                        .associateBy { it.link }
                        .values
                        .mapNotNull { importDeepLink(it, it.folderId?.let(folders::get)) }

                    shortcutManager.enable(createdIds)
                }

                FileType.TXT -> {
                    val deepLinksTexts = fileContents.split("\n").distinct()

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
    private fun importFolders(fileFolders: List<Folder>): Map<String, Folder> {
        val localFolders = folderRepository.getFolders()
        val localByName = localFolders.associateBy { it.name }
        val localById = localFolders.associateBy { it.id }
        val fileFoldersByName = fileFolders.groupBy { it.name }
        val takenIds = fileFoldersByName.keys.mapNotNull { localByName[it]?.id }.toMutableSet()

        return fileFoldersByName.flatMap { (name, sameNameFolders) ->
            val candidateIds = sameNameFolders.map { it.id }.distinct()
            val id = localByName[name]?.id
                ?: candidateIds.firstOrNull { it !in takenIds && (candidateIds.size == 1 || it !in localById) }
                ?: Uuid.random().toString()
            takenIds += id
            val folder = Folder(
                id = id,
                name = name,
                description = sameNameFolders.mapNotNull { it.description }.lastOrNull()
                    ?: localById[id]?.description,
            )
            val savedFolder = when (val result = folderRepository.upsertFolder(folder)) {
                FolderRepository.UpsertResult.Saved -> folder
                is FolderRepository.UpsertResult.NameAlreadyExists -> folder.copy(id = result.existingId)
            }
            sameNameFolders.map { it.id to savedFolder }
        }.toMap()
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun importDeepLink(dto: Payload.DeepLink, folder: Folder?): String? {
        val local = deepLinkRepository.getDeepLinkByLink(dto.link)
        if (local != null) {
            deepLinkRepository.upsertDeepLink(
                local.copy(
                    name = dto.name ?: local.name,
                    description = dto.description ?: local.description,
                    isFavorite = dto.isFavorite ?: local.isFavorite,
                    createdAt = dto.createdAt?.let { LocalDateTime.parse(it) } ?: local.createdAt,
                    folder = folder ?: local.folder,
                    targetPackage = dto.targetPackage ?: local.targetPackage,
                ),
            )
            return null
        }

        val imported = dto.toModel(folder)
        val deepLink = if (deepLinkRepository.getDeepLinkById(imported.id) == null) {
            imported
        } else {
            imported.copy(id = Uuid.random().toString())
        }

        return when (deepLinkRepository.upsertDeepLink(deepLink)) {
            DeepLinkRepository.UpsertResult.Saved -> deepLink.id
            is DeepLinkRepository.UpsertResult.LinkAlreadyExists -> null
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
