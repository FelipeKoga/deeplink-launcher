package dev.koga.deeplinklauncher.deeplink.impl.usecase

import android.content.Context
import android.util.LruCache
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinkHandlerIcon

internal class GetDeepLinkHandlerIconImpl(
    private val context: Context,
) : GetDeepLinkHandlerIcon {
    private val cache = LruCache<String, ByteArray>(CACHE_SIZE)

    override suspend fun invoke(link: String): ByteArray? {
        val key = link.trim()
        cache.get(key)?.let { return it }
        val icon = context.resolveHandlerIconPng(key) ?: return null
        cache.put(key, icon)
        return icon
    }

    companion object {
        private const val CACHE_SIZE = 128
    }
}
