package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.PinDeepLinkToHomeScreen
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class PinDeepLinkToHomeScreenImpl : PinDeepLinkToHomeScreen {
    override fun invoke(deepLink: DeepLink): PinDeepLinkToHomeScreen.Result {
        return PinDeepLinkToHomeScreen.Result.NotSupported
    }
}
