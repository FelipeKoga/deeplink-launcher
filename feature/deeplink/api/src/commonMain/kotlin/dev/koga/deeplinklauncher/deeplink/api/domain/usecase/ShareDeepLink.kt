package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink

public interface ShareDeepLink {
    public operator fun invoke(deepLink: DeepLink)
}
