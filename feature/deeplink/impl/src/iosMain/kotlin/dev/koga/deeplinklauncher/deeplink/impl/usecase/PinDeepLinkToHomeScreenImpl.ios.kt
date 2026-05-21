package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.PinDeepLinkToHomeScreen

internal class PinDeepLinkToHomeScreenImpl : PinDeepLinkToHomeScreen {
    override fun invoke(deepLink: DeepLink): PinDeepLinkToHomeScreen.Result {
        return PinDeepLinkToHomeScreen.Result.NotSupported
    }
}
