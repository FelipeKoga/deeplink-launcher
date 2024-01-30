package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.constant.defaultDeepLink
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GetDeepLinksJsonPreview(
    private val dataSource: DeepLinkDataSource,
) {

    @Serializable
    private data class DeepLinkJson(
        val id: String,
        val link: String,
        val name: String?,
        val description: String?,
        val isFavorite: Boolean,
        val folder: FolderJson?,
    ) {
        @Serializable
        data class FolderJson(
            val id: String,
            val name: String,
            val description: String?,
        )
    }

    operator fun invoke(): String {
        val deepLinks = dataSource.getDeepLinks()
            .filter { it != defaultDeepLink }
            .map {
                DeepLinkJson(
                    id = it.id,
                    link = it.link,
                    name = it.name,
                    description = it.description,
                    isFavorite = it.isFavorite,
                    folder = it.folder?.let { folder ->
                        DeepLinkJson.FolderJson(
                            id = folder.id,
                            name = folder.name,
                            description = folder.description,
                        )
                    },
                )
            }

        val json = Json { prettyPrint = true }

        return json.encodeToString(deepLinks)
    }
}
