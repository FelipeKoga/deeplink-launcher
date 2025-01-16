package dev.koga.deeplinklauncher.importexport.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.importexport.dto.dateFormat
import dev.koga.deeplinklauncher.importexport.usecase.GetDeepLinksJsonPreview
import kotlinx.datetime.format
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

internal class DefaultGetDeepLinksJsonPreview(
    private val deepLinkRepository: DeepLinkRepository,
    private val folderRepository: FolderRepository,
) : GetDeepLinksJsonPreview {

    override operator fun invoke(): String {
        val deepLinks = deepLinkRepository.getDeepLinks()
        val folders = folderRepository.getFolders()

        if (deepLinks.isEmpty() && folders.isEmpty()) return ""

        val json = Json { prettyPrint = true }

        return json.encodeToString(
            ExportJson(
                folders = folders.map {
                    ExportJson.Folder(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                    )
                },
                deepLinks = deepLinks.map {
                    ExportJson.DeepLink(
                        id = it.id,
                        link = it.link,
                        name = it.name,
                        description = it.description,
                        isFavorite = it.isFavorite,
                        createdAt = it.createdAt.format(dateFormat),
                        folderId = it.folder?.id,
                    )
                },
            ),
        )
    }

    @Serializable
    private data class ExportJson(
        val folders: List<Folder>,
        val deepLinks: List<DeepLink>,
    ) {
        @Serializable
        data class DeepLink(
            val id: String,
            val link: String,
            val name: String?,
            val description: String?,
            val isFavorite: Boolean,
            val createdAt: String?,
            val folderId: String?,
        )

        @Serializable
        data class Folder(
            val id: String,
            val name: String,
            val description: String?,
        )
    }
}
