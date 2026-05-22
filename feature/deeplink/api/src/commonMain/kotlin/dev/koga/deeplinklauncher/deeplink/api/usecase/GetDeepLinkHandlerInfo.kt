package dev.koga.deeplinklauncher.deeplink.api.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkHandlerInfo

public interface GetDeepLinkHandlerInfo {
    public operator fun invoke(link: String): DeepLinkHandlerInfo
}
