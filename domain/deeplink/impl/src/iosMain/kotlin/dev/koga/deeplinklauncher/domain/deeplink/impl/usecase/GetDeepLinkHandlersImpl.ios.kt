package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkHandler
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkHandlers
internal class GetDeepLinkHandlersImpl : GetDeepLinkHandlers {
    override suspend fun invoke(link: String): List<DeepLinkHandler> = emptyList()
}
