package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.PinDeepLinkToHomeScreen

internal class PinDeepLinkToHomeScreenImpl : PinDeepLinkToHomeScreen {
    override fun invoke(deepLink: DeepLink): PinDeepLinkToHomeScreen.Result {
        return PinDeepLinkToHomeScreen.Result.NotSupported
    }
}
