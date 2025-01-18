package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.usecase.ValidateDeepLink

expect class ValidateDeepLinkImpl : ValidateDeepLink {
    override fun isValid(link: String): Boolean
}
