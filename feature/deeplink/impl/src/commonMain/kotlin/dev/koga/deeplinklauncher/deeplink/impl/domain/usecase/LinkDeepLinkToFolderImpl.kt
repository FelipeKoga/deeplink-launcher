package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.LinkDeepLinkToFolder

internal class LinkDeepLinkToFolderImpl(
    private val deepLinkRepository: DeepLinkRepository,
    private val folderRepository: FolderRepository,
) : LinkDeepLinkToFolder {

    override suspend fun invoke(deepLinkId: String, folderId: String): LinkDeepLinkToFolder.Result {
        val deepLink = deepLinkRepository.getDeepLinkById(deepLinkId)
            ?: return LinkDeepLinkToFolder.Result.NotFound
        val folder = folderRepository.getFolderById(folderId)
            ?: return LinkDeepLinkToFolder.Result.NotFound

        if (deepLink.folder?.id == folderId) {
            return LinkDeepLinkToFolder.Result.AlreadyLinked
        }

        deepLinkRepository.upsertDeepLink(deepLink.copy(folder = folder))
        return LinkDeepLinkToFolder.Result.Linked
    }
}
