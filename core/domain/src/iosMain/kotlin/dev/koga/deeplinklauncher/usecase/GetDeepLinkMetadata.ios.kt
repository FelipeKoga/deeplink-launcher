package dev.koga.deeplinklauncher.usecase

import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents

actual class GetDeepLinkMetadata {
    actual fun execute(link: String): DeepLinkMetadata {
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
