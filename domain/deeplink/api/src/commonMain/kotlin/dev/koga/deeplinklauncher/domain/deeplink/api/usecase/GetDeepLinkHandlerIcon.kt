package dev.koga.deeplinklauncher.domain.deeplink.api.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkIcon
public interface GetDeepLinkHandlerIcon {
    public suspend operator fun invoke(link: String, targetPackage: String? = null): DeepLinkIcon?
}
