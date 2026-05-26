package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandler

public interface GetDeepLinkHandlers {
    public suspend operator fun invoke(link: String): List<DeepLinkHandler>
}
