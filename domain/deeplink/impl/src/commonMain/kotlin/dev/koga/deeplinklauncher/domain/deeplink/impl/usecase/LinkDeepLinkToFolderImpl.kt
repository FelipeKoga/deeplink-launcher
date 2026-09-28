package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.LinkDeepLinkToFolder
internal class LinkDeepLinkToFolderImpl(
    private val deepLinkRepository: DeepLinkRepository,
    private val folderRepository: FolderRepository,
) : LinkDeepLinkToFolder {

    override suspend fun invoke(deepLinkId: String, folderId: String): LinkDeepLinkToFolder.Result {
        val deepLink = deepLinkRepository.getDeepLinkById(deepLinkId)
            ?: return LinkDeepLinkToFolder.Result.NotFound
        folderRepository.getFolderById(folderId)
            ?: return LinkDeepLinkToFolder.Result.NotFound

        if (deepLink.folder?.id == folderId) {
            return LinkDeepLinkToFolder.Result.AlreadyLinked
        }

        deepLinkRepository.setFolder(id = deepLinkId, folderId = folderId)
        return LinkDeepLinkToFolder.Result.Linked
    }
}
