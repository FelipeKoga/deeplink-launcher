package dev.koga.deeplinklauncher.domain.deeplink.api.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkHandlerInfo
public interface GetDeepLinkHandlerInfo {
    public suspend operator fun invoke(link: String, targetPackage: String? = null): DeepLinkHandlerInfo
}
