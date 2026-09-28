package dev.koga.deeplinklauncher.home.impl.domain

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

/** Home's search over deeplinks and folders. Home is its only consumer, so it lives here. */
internal class GetDeepLinksAndFolderStream(
    private val repository: DeepLinkRepository,
    private val folderRepository: FolderRepository,
) {

    operator fun invoke(query: String): Flow<Result> {
        val normalizeQuery = query.trim()

        return combine(
            repository.getDeepLinksStream().distinctUntilChanged(),
            folderRepository.getFoldersStream().distinctUntilChanged(),
        ) { deepLinks, folders ->
            val filteredDeepLinks = filterDeepLinks(deepLinks, normalizeQuery)
            Result(
                deepLinks = filteredDeepLinks,
                favorites = filteredDeepLinks.filter(DeepLink::isFavorite),
                folders = filterFolders(folders, normalizeQuery),
                folderPreviewDeepLinks = deepLinks.filter { it.folder != null },
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

    data class Result(
        val deepLinks: List<DeepLink>,
        val favorites: List<DeepLink>,
        val folders: List<Folder>,
        val folderPreviewDeepLinks: List<DeepLink>,
    )
}
