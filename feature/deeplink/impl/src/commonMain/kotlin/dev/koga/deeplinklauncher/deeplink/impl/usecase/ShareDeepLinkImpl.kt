package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.ShareDeepLink

expect class ShareDeepLinkImpl : ShareDeepLink {
    override fun invoke(deepLink: DeepLink)
}