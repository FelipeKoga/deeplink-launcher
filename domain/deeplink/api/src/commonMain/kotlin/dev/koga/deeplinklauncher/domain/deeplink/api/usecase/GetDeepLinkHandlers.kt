package dev.koga.deeplinklauncher.domain.deeplink.api.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkHandler
public interface GetDeepLinkHandlers {
    public suspend operator fun invoke(link: String): List<DeepLinkHandler>
}
