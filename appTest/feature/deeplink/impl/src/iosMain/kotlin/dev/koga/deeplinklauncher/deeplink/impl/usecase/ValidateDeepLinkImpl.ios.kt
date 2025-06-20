package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.usecase.ValidateDeepLink
import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents

actual class ValidateDeepLinkImpl : ValidateDeepLink {
    actual override fun isValid(link: String): Boolean {
        val nsurl = NSURL(string = link)

        val components = NSURLComponents.componentsWithURL(
            url = nsurl,
            resolvingAgainstBaseURL = false,
        )

        return components?.scheme != null
    }
}
