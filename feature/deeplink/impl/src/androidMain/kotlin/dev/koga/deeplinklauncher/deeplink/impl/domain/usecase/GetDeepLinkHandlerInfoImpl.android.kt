package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import android.content.Context
import android.util.LruCache
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.impl.platform.android.createDeepLinkViewIntent
import dev.koga.deeplinklauncher.deeplink.impl.platform.android.handlerCacheKey

internal class GetDeepLinkHandlerInfoImpl(
    private val context: Context,
) : GetDeepLinkHandlerInfo {
    private val cache = LruCache<String, DeepLinkHandlerInfo>(CACHE_SIZE)

    override suspend fun invoke(
        link: String,
        targetPackage: String?,
    ): DeepLinkHandlerInfo {
        val key = handlerCacheKey(link, targetPackage)
        cache.get(key)?.let { return it }

        val intent = context.createDeepLinkViewIntent(link, targetPackage)
        val resolveInfo = context.packageManager.resolveActivity(intent, 0)

        return DeepLinkHandlerInfo.Available(
            canResolve = resolveInfo != null,
            appName = resolveInfo?.loadLabel(context.packageManager)?.toString(),
            packageName = resolveInfo?.activityInfo?.packageName ?: targetPackage,
        ).also { cache.put(key, it) }
    }

    companion object {
        private const val CACHE_SIZE = 128
    }
}
