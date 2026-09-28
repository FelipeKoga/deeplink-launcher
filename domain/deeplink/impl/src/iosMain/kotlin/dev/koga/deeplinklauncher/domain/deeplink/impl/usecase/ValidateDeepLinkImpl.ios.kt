package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.ValidateDeepLink
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
