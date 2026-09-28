package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.DeleteAllDeepLinks
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
