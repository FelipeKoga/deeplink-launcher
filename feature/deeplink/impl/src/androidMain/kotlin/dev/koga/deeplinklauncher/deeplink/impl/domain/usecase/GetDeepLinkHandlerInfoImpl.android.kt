package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.impl.platform.android.AndroidHandlerResolver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
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
