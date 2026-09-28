package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.DeleteAllDeepLinks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

internal class DeleteAllDeepLinksImpl(
    private val repository: DeepLinkRepository,
    private val shortcutManager: DeepLinkShortcutManager,
) : DeleteAllDeepLinks {

    override suspend fun invoke() {
        withContext(Dispatchers.IO + NonCancellable) {
            val ids = repository.getDeepLinks().map(DeepLink::id)
            repository.deleteAll()
            shortcutManager.disable(ids)
        }
    }
}
