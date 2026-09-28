package dev.koga.deeplinklauncher.datatransfer.impl.domain.usecase

import dev.koga.deeplinklauncher.datatransfer.api.domain.usecase.ImportDeepLinks
import dev.koga.deeplinklauncher.datatransfer.impl.data.dto.Payload
import dev.koga.deeplinklauncher.datatransfer.impl.data.dto.toModel
import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ValidateDeepLink
import dev.koga.deeplinklauncher.file.GetFileContent
import dev.koga.deeplinklauncher.file.model.FileType
import kotlinx.coroutines.CancellationException
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Parses, validates and maps the whole file before writing anything, then hands the
 * result to [DeepLinkRepository.importAll], which applies it in a single transaction.
 * A malformed entry therefore leaves the database untouched.
 */
internal class ImportDeepLinksImpl(
    private val getFileContent: GetFileContent,
    private val deepLinkRepository: DeepLinkRepository,
    private val validateDeepLink: ValidateDeepLink,
    private val shortcutManager: DeepLinkShortcutManager,
) : ImportDeepLinks {

    override suspend operator fun invoke(
        filePath: String,
        fileType: FileType,
    ): ImportDeepLinks.Result {
        return try {
            val fileContents = getFileContent(filePath)
            val plan = when (fileType) {
                FileType.JSON -> planJson(fileContents)
                FileType.TXT -> planText(fileContents)
            }

            when (plan) {
                is Plan.Invalid -> ImportDeepLinks.Result.Error.InvalidDeepLinksFound(plan.links)
                is Plan.Apply -> {
                    deepLinkRepository.importAll(folders = plan.folders, deepLinks = plan.deepLinks)
                    if (plan.newDeepLinkIds.isNotEmpty()) shortcutManager.enable(plan.newDeepLinkIds)
                    ImportDeepLinks.Result.Success
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            ImportDeepLinks.Result.Error.Unknown
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun planJson(fileContents: String): Plan {
        val json = Json {
            ignoreUnknownKeys = true
            allowTrailingComma = true
        }
        val payload = json.decodeFromString<Payload>(fileContents)

        val invalidLinks = payload.deepLinks.map { it.link }.filterNot(validateDeepLink::isValid)
        if (invalidLinks.isNotEmpty()) return Plan.Invalid(invalidLinks)

        val folders = payload.folders.orEmpty().map(Payload.Folder::toModel)
        val foldersById = folders.associateBy(Folder::id)

        val newDeepLinkIds = mutableListOf<String>()
        val deepLinks = payload.deepLinks.map { dto ->
            val existing = deepLinkRepository.getDeepLinkByLink(dto.link)
            val folder = dto.folderId?.let(foldersById::get)

            if (existing == null) {
                dto.toModel(folder).also { newDeepLinkIds += it.id }
            } else {
                // Fields missing from the file keep their local value.
                existing.copy(
                    name = dto.name ?: existing.name,
                    description = dto.description ?: existing.description,
                    isFavorite = dto.isFavorite ?: existing.isFavorite,
                    createdAt = dto.createdAt?.let(LocalDateTime::parse) ?: existing.createdAt,
                    folder = folder ?: existing.folder,
                    targetPackage = dto.targetPackage ?: existing.targetPackage,
                )
            }
        }

        return Plan.Apply(folders = folders, deepLinks = deepLinks, newDeepLinkIds = newDeepLinkIds)
    }

    private fun planText(fileContents: String): Plan {
        val links = fileContents.lineSequence()
            .map(String::trim)
            .filter(String::isNotEmpty)
            .distinct()
            .toList()

        val newLinks = links.filter { deepLinkRepository.getDeepLinkByLink(it) == null }

        val invalidLinks = newLinks.filterNot(validateDeepLink::isValid)
        if (invalidLinks.isNotEmpty()) return Plan.Invalid(invalidLinks)

        return Plan.Apply(folders = emptyList(), deepLinks = newLinks.map { it.toDeepLink() })
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun String.toDeepLink(): DeepLink {
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

    private sealed interface Plan {
        data class Invalid(val links: List<String>) : Plan
        data class Apply(
            val folders: List<Folder>,
            val deepLinks: List<DeepLink>,
            val newDeepLinkIds: List<String> = emptyList(),
        ) : Plan
    }
}
