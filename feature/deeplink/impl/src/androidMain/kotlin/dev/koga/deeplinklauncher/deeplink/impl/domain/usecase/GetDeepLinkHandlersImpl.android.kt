package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import android.content.Context
import android.content.pm.PackageManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandler
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlers
import dev.koga.deeplinklauncher.deeplink.impl.platform.android.createDeepLinkViewIntent

internal class GetDeepLinkHandlersImpl(
    private val context: Context,
) : GetDeepLinkHandlers {
    override suspend fun invoke(link: String): List<DeepLinkHandler> {
        val intent = createDeepLinkViewIntent(link)
        return context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
            .map { resolveInfo ->
                DeepLinkHandler(
                    packageName = resolveInfo.activityInfo.packageName,
                    appName = resolveInfo.loadLabel(context.packageManager).toString(),
                )
            }
            .distinctBy { it.packageName }
            .sortedBy { it.appName.lowercase() }
    }
}
