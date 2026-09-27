package dev.koga.deeplinklauncher.deeplink.api.domain.manager

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink

public interface DeepLinkShortcutManager {
    public suspend fun isAdded(deepLinkId: String): Boolean
    public suspend fun add(deepLink: DeepLink): AddResult
    public suspend fun update(deepLink: DeepLink)
    public suspend fun remove(deepLinkId: String)
    public suspend fun disable(deepLinkIds: List<String>)

    public sealed interface AddResult {
        public data object Added : AddResult
        public data object NotSupported : AddResult
    }
}
