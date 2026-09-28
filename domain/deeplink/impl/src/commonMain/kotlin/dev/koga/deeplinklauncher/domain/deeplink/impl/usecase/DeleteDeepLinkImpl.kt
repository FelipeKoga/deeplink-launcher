package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.domain.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.DeleteDeepLink
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

internal class DeleteDeepLinkImpl(
    private val repository: DeepLinkRepository,
    private val shortcutManager: DeepLinkShortcutManager,
) : DeleteDeepLink {

    override suspend fun invoke(id: String) {
        withContext(NonCancellable) {
            repository.delete(id)
            shortcutManager.disable(listOf(id))
        }
    }
}
