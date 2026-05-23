package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkIcon

public interface GetDeepLinkHandlerIcon {
    public suspend operator fun invoke(link: String): DeepLinkIcon?
}
