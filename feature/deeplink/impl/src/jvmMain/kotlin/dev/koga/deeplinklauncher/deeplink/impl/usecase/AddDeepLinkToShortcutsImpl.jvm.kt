package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.AddDeepLinkToShortcuts

internal class AddDeepLinkToShortcutsImpl : AddDeepLinkToShortcuts {
    override fun invoke(deepLink: DeepLink): AddDeepLinkToShortcuts.Result {
        return AddDeepLinkToShortcuts.Result.NotSupported
    }
}
