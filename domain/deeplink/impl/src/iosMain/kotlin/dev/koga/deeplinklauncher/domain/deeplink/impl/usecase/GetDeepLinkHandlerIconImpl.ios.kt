package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkIcon
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkHandlerIcon
internal class GetDeepLinkHandlerIconImpl : GetDeepLinkHandlerIcon {
    override suspend fun invoke(
        link: String,
        targetPackage: String?,
    ): DeepLinkIcon? {
        return null
    }
}
