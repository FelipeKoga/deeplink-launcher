package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkIcon
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.domain.deeplink.impl.platform.android.AndroidHandlerResolver
internal class GetDeepLinkHandlerIconImpl(
    private val resolver: AndroidHandlerResolver,
) : GetDeepLinkHandlerIcon {

    override suspend fun invoke(
        link: String,
        targetPackage: String?,
    ): DeepLinkIcon? {
        return resolver.resolve(link, targetPackage)?.icon
    }
}
