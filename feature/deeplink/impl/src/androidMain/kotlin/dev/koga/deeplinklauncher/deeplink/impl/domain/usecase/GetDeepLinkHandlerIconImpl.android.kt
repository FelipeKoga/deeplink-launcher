package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import android.content.Context
import android.util.LruCache
import dev.koga.deeplinklauncher.deeplink.impl.platform.android.resolveHandlericon
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerIcon

internal class GetDeepLinkHandlerIconImpl(
    private val context: Context,
) : GetDeepLinkHandlerIcon {
    private val cache = LruCache<String, ByteArray>(CACHE_SIZE)

    override suspend fun invoke(link: String): ByteArray? {
        val key = link.trim()
        cache.get(key)?.let { return it }
        val icon = context.resolveHandlericon(key) ?: return null
        cache.put(key, icon)
        return icon
    }

    companion object {
        private const val CACHE_SIZE = 128
    }
}
