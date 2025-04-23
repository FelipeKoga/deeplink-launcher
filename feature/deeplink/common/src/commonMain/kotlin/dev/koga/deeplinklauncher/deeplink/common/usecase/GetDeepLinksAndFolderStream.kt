package dev.koga.deeplinklauncher.deeplink.common.usecase

import dev.koga.deeplinklauncher.deeplink.common.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.common.model.Folder
import dev.koga.deeplinklauncher.deeplink.common.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.common.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

public class GetDeepLinksAndFolderStream(
    private val repository: DeepLinkRepository,
    private val folderRepository: FolderRepository,
) {

    public data class Output(
        val deepLinks: List<DeepLink>,
        val favorites: List<DeepLink>,
        val folders: List<Folder>,
    )

    public operator fun invoke(query: String): Flow<Output> {
        val normalizeQuery = query.trim()

        return combine(
            repository.getDeepLinksStream(),
            folderRepository.getFoldersStream(),
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
}
