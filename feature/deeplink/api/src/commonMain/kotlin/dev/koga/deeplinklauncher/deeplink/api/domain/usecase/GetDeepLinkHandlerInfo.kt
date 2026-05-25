package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo

public interface GetDeepLinkHandlerInfo {
    public suspend operator fun invoke(link: String): DeepLinkHandlerInfo
}
