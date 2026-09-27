package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkIcon
import dev.koga.deeplinklauncher.deeplink.impl.platform.android.AndroidHandlerResolver

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
