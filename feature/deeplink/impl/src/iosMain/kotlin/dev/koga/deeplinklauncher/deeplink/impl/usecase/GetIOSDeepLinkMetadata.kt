package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinkMetadata
import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents

internal class GetIOSDeepLinkMetadata : GetDeepLinkMetadata {
    override fun invoke(link: String): DeepLinkMetadata {
        val nsurl = NSURL(string = link)

        val components = NSURLComponents.componentsWithURL(
            url = nsurl,
            resolvingAgainstBaseURL = false,
        ) ?: return DeepLinkMetadata(
            link = link,
            scheme = null,
            query = null,
            host = null,
        )

        return DeepLinkMetadata(
            link = link,
            scheme = components.scheme,
            query = components.query,
            host = components.host,
        )
    }
}