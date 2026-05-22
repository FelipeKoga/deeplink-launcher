package dev.koga.deeplinklauncher.deeplink.impl.usecase

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinkHandlerInfo

internal class GetDeepLinkHandlerInfoImpl(
    private val context: Context,
) : GetDeepLinkHandlerInfo {
    override fun invoke(link: String): DeepLinkHandlerInfo {
        val intent = Intent(Intent.ACTION_VIEW, link.trim().toUri())
        val resolveInfo = context.packageManager.resolveActivity(intent, 0)

        return DeepLinkHandlerInfo(
            canResolve = resolveInfo != null,
            appName = resolveInfo?.loadLabel(context.packageManager)?.toString(),
        )
    }
}
