package dev.koga.deeplinklauncher.deeplink.common.usecase

import dev.koga.deeplinklauncher.deeplink.common.model.DeepLinkMetadata
import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents

internal class GetDeepLinkMetadataImpl : GetDeepLinkMetadata {
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