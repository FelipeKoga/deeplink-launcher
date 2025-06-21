package dev.koga.deeplinklauncher.deeplink.api.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink

public interface ShareDeepLink {
    public operator fun invoke(deepLink: DeepLink)
}
