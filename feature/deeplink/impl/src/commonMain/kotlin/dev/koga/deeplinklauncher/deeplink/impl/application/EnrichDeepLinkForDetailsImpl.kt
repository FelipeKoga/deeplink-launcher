package dev.koga.deeplinklauncher.deeplink.impl.application

import dev.koga.deeplinklauncher.coroutines.AppDispatchers
import dev.koga.deeplinklauncher.deeplink.api.application.EnrichDeepLinkForDetails
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkDetailsModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.withContext

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class EnrichDeepLinkForDetailsImpl(
    private val getDeepLinkHandlerIcon: GetDeepLinkHandlerIcon,
    private val getDeepLinkMetadata: GetDeepLinkMetadata,
    private val getDeepLinkHandlerInfo: GetDeepLinkHandlerInfo,
    private val dispatchers: AppDispatchers,
) : EnrichDeepLinkForDetails {
    override suspend fun invoke(deepLink: DeepLink): DeepLinkDetailsModel =
        withContext(dispatchers.io) {
            DeepLinkDetailsModel(
                deepLink = deepLink,
                icon = getDeepLinkHandlerIcon(deepLink.link, deepLink.targetPackage),
                metadata = getDeepLinkMetadata(deepLink.link),
                handlerInfo = getDeepLinkHandlerInfo(deepLink.link, deepLink.targetPackage),
            )
        }
}
