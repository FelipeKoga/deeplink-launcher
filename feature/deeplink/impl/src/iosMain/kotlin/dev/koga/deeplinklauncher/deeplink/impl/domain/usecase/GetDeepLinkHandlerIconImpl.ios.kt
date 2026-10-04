package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkIcon
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class GetDeepLinkHandlerIconImpl : GetDeepLinkHandlerIcon {
    override suspend fun invoke(
        link: String,
        targetPackage: String?,
    ): DeepLinkIcon? {
        return null
    }
}
