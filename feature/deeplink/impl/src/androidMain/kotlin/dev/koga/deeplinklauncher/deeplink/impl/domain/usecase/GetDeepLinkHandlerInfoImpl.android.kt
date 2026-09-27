package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.impl.platform.android.AndroidHandlerResolver

internal class GetDeepLinkHandlerInfoImpl(
    private val resolver: AndroidHandlerResolver,
) : GetDeepLinkHandlerInfo {

    override suspend fun invoke(
        link: String,
        targetPackage: String?,
    ): DeepLinkHandlerInfo {
        val handler = resolver.resolve(link, targetPackage)

        return DeepLinkHandlerInfo.Available(
            canResolve = handler != null,
            appName = handler?.label,
            packageName = handler?.packageName ?: targetPackage,
        )
    }
}
