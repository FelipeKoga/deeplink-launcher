package dev.koga.deeplinklauncher.deeplink.common.usecase

import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents

internal class ValidateDeepLinkImpl : ValidateDeepLink {
    override fun isValid(link: String): Boolean {
        val nsurl = NSURL(string = link)

        val components = NSURLComponents.componentsWithURL(
            url = nsurl,
            resolvingAgainstBaseURL = false,
        )

        return components?.scheme != null
    }
}