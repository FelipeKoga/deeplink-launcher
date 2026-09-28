package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkHandlerInfo
import dev.koga.deeplinklauncher.domain.deeplink.impl.platform.android.AndroidHandlerResolver
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
