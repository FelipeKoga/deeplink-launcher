package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkMetadata
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
            host = null,
            path = "/",
            query = null,
        )

        return DeepLinkMetadata(
            link = link,
            scheme = components.scheme,
            host = components.host,
            path = components.path ?: "/",
            query = components.query,
        )
    }
}
