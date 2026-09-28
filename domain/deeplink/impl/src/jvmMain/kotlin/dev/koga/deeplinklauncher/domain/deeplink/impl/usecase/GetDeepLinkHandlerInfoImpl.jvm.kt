package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkHandlerInfo
internal class GetDeepLinkHandlerInfoImpl : GetDeepLinkHandlerInfo {
    override suspend fun invoke(
        link: String,
        targetPackage: String?,
    ): DeepLinkHandlerInfo {
        return DeepLinkHandlerInfo.Unavailable
    }
}
