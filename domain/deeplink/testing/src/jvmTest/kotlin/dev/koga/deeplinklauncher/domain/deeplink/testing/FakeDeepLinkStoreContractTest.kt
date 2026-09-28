package dev.koga.deeplinklauncher.domain.deeplink.testing

import dev.koga.deeplinklauncher.domain.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.FolderRepository

/** The fake must behave like the SQL implementation; see DeepLinkRepositoryContract. */
class FakeDeepLinkStoreContractTest : DeepLinkRepositoryContract() {
    override fun createRepositories(): Pair<DeepLinkRepository, FolderRepository> =
        FakeDeepLinkStore().let { it.deepLinkRepository to it.folderRepository }
}
