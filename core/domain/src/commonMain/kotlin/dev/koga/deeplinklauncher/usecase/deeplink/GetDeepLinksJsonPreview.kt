package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.constant.defaultDeepLink
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.FolderDataSource
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GetDeepLinksJsonPreview(
    private val deepLinkDataSource: DeepLinkDataSource,
    private val folderDataSource: FolderDataSource,
) {

    operator fun invoke(): String {
        val deepLinks = deepLinkDataSource.getDeepLinks().filter { it.id != defaultDeepLink.id }
        val folders = folderDataSource.getFolders()

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
