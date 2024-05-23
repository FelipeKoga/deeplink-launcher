package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.FolderDataSource
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetDeepLinksAndFolderStream(
    private val deepLinkDataSource: DeepLinkDataSource,
    private val folderDataSource: FolderDataSource,
) {
    operator fun invoke(query: String): Flow<Output> {
        val normalizeQuery = query.trim()

        return combine(
            deepLinkDataSource.getDeepLinksStream(),
            folderDataSource.getFoldersStream(),
        ) { deepLinks, folders ->
            Output(
                deepLinks = filterDeepLinks(deepLinks, normalizeQuery),
                favorites = filterDeepLinks(deepLinks, normalizeQuery).filter(DeepLink::isFavorite),
                folders = filterFolders(folders, normalizeQuery),
            )
        }
    }

    private fun filterDeepLinks(deepLinks: List<DeepLink>, query: String): List<DeepLink> {
        return deepLinks.filter {
            it.link.contains(query, true)
        }
    }

    private fun filterFolders(folders: List<Folder>, query: String): List<Folder> {
        return folders.filter { it.name.contains(query, true) }
    }

    data class Output(
        val deepLinks: List<DeepLink>,
        val favorites: List<DeepLink>,
        val folders: List<Folder>,
    )
}
