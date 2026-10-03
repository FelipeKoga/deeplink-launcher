package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandler
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlers
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class GetDeepLinkHandlersImpl : GetDeepLinkHandlers {
    override suspend fun invoke(link: String): List<DeepLinkHandler> = emptyList()
}
