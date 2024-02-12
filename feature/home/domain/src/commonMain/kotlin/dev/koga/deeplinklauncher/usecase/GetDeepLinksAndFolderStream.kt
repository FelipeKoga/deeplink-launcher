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
    operator fun invoke(): Flow<Output> {
        return combine(
            deepLinkDataSource.getDeepLinksStream(),
            folderDataSource.getFoldersStream(),
        ) { deepLinks, folders ->
            Output(
                deepLinks = deepLinks,
                favorites = deepLinks.filter(DeepLink::isFavorite),
                folders = folders,
            )
        }
    }

    data class Output(
        val deepLinks: List<DeepLink>,
        val favorites: List<DeepLink>,
        val folders: List<Folder>,
    )
}
