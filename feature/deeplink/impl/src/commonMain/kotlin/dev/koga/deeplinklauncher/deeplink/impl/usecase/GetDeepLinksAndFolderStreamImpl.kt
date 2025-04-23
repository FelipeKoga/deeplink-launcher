package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinksAndFolderStream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * :deeplink:ui
 * :deeplink:common
 *
 * :folder:ui
 * :folder:common
 */

class GetDeepLinksAndFolderStreamImpl(
    private val repository: DeepLinkRepository,
    private val folderRepository: FolderRepository,
) : GetDeepLinksAndFolderStream {

    override operator fun invoke(query: String): Flow<GetDeepLinksAndFolderStream.Result> {
        val normalizeQuery = query.trim()

        return combine(
            repository.getDeepLinksStream(),
            folderRepository.getFoldersStream(),
        ) { deepLinks, folders ->
            GetDeepLinksAndFolderStream.Result(
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
