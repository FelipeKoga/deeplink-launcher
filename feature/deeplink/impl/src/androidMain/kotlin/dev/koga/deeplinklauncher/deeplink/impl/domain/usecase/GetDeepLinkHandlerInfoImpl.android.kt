package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import android.content.Context
import android.content.Intent
import android.util.LruCache
import androidx.core.net.toUri
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerInfo

internal class GetDeepLinkHandlerInfoImpl(
    private val context: Context,
) : GetDeepLinkHandlerInfo {
    private val cache = LruCache<String, DeepLinkHandlerInfo>(CACHE_SIZE)

    override fun invoke(link: String): DeepLinkHandlerInfo {
        val key = link.trim()
        cache.get(key)?.let { return it }

        val intent = Intent(Intent.ACTION_VIEW, key.toUri())
        val resolveInfo = context.packageManager.resolveActivity(intent, 0)

        return DeepLinkHandlerInfo(
            canResolve = resolveInfo != null,
            appName = resolveInfo?.loadLabel(context.packageManager)?.toString(),
        ).also { cache.put(key, it) }
    }

    companion object {
        private const val CACHE_SIZE = 128
    }
}
