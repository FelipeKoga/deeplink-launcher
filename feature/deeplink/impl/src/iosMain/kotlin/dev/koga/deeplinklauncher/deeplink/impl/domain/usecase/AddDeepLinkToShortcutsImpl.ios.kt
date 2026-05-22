package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.AddDeepLinkToShortcuts

internal class AddDeepLinkToShortcutsImpl : AddDeepLinkToShortcuts {
    override fun invoke(deepLink: DeepLink): AddDeepLinkToShortcuts.Result {
        return AddDeepLinkToShortcuts.Result.NotSupported
    }
}
