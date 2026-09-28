package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.DeleteAllDeepLinks
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

internal class DeleteAllDeepLinksImpl(
    private val repository: DeepLinkRepository,
    private val shortcutManager: DeepLinkShortcutManager,
) : DeleteAllDeepLinks {

    override suspend fun invoke() {
        withContext(NonCancellable) {
            val ids = repository.deleteAll()
            shortcutManager.disable(ids)
        }
    }
}
