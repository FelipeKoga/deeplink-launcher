package dev.koga.deeplinklauncher.deeplink.impl.domain.manager

import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class DeepLinkShortcutManagerImpl : DeepLinkShortcutManager {
    override suspend fun isAdded(deepLinkId: String): Boolean = false

    override suspend fun add(deepLink: DeepLink): DeepLinkShortcutManager.AddResult =
        DeepLinkShortcutManager.AddResult.NotSupported

    override suspend fun update(deepLink: DeepLink) = Unit

    override suspend fun remove(deepLinkId: String) = Unit

    override suspend fun enable(deepLinkIds: List<String>) = Unit

    override suspend fun disable(deepLinkIds: List<String>) = Unit
}
