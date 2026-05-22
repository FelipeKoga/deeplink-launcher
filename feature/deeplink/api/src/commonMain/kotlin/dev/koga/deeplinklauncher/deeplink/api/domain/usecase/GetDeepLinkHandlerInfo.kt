package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandlerInfo

public interface GetDeepLinkHandlerInfo {
    public operator fun invoke(link: String): DeepLinkHandlerInfo
}
