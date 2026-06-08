package dev.koga.deeplinklauncher.deeplink.api.application

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkDetailsModel

public interface EnrichDeepLinkForDetails {
    public suspend operator fun invoke(deepLink: DeepLink): DeepLinkDetailsModel
}
