package dev.koga.deeplinklauncher.domain.deeplink.impl.manager
import dev.koga.deeplinklauncher.domain.deeplink.api.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
internal class DeepLinkShortcutManagerImpl : DeepLinkShortcutManager {
    override suspend fun isAdded(deepLinkId: String): Boolean = false

    override suspend fun add(deepLink: DeepLink): DeepLinkShortcutManager.AddResult =
        DeepLinkShortcutManager.AddResult.NotSupported

    override suspend fun update(deepLink: DeepLink) = Unit

    override suspend fun remove(deepLinkId: String) = Unit

    override suspend fun enable(deepLinkIds: List<String>) = Unit

    override suspend fun disable(deepLinkIds: List<String>) = Unit
}
