package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.ValidateDeepLink
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class ValidateDeepLinkImpl : ValidateDeepLink {
    override fun isValid(link: String): Boolean {
        val nsurl = NSURL.URLWithString(link) ?: return false

        val components = NSURLComponents.componentsWithURL(
            url = nsurl,
            resolvingAgainstBaseURL = false,
        )

        return components?.scheme != null
    }
}
