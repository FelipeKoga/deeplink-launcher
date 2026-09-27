package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinksAndFolderStream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

internal class GetDeepLinksAndFolderStreamImpl(
    private val repository: DeepLinkRepository,
    private val folderRepository: FolderRepository,
) : GetDeepLinksAndFolderStream {

    override operator fun invoke(query: String): Flow<GetDeepLinksAndFolderStream.Result> {
        val normalizeQuery = query.trim()

        return combine(
            repository.getDeepLinksStream().distinctUntilChanged(),
            folderRepository.getFoldersStream().distinctUntilChanged(),
        ) { deepLinks, folders ->
            val filteredDeepLinks = filterDeepLinks(deepLinks, normalizeQuery)
            GetDeepLinksAndFolderStream.Result(
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
}
