package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import android.content.Context
import android.util.LruCache
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkIcon
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.deeplink.impl.platform.android.resolveHandlericon

internal class GetDeepLinkHandlerIconImpl(
    private val context: Context,
) : GetDeepLinkHandlerIcon {
    private val cache = LruCache<String, ByteArray>(CACHE_SIZE)

    override suspend fun invoke(link: String): DeepLinkIcon? {
        val key = link.trim()
        cache.get(key)?.let { return DeepLinkIcon(it) }
        val icon = context.resolveHandlericon(key) ?: return null
        cache.put(key, icon)
        return DeepLinkIcon(icon)
    }

    companion object {
        private const val CACHE_SIZE = 128
    }
}
