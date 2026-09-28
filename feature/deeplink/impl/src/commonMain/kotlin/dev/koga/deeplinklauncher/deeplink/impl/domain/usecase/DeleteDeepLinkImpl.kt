package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.DeleteDeepLink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

internal class DeleteDeepLinkImpl(
    private val repository: DeepLinkRepository,
    private val shortcutManager: DeepLinkShortcutManager,
) : DeleteDeepLink {

    override suspend fun invoke(id: String) {
        withContext(Dispatchers.IO + NonCancellable) {
            repository.deleteDeepLink(id)
            shortcutManager.disable(listOf(id))
        }
    }
}
