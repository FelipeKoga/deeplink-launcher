package dev.koga.deeplinklauncher.deeplink.common.usecase

import dev.koga.deeplinklauncher.deeplink.common.model.DeepLink

public interface ShareDeepLink {
    public fun invoke(deepLink: DeepLink)
}
