package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkMetadata
import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents

internal class GetDeepLinkMetadataImpl : GetDeepLinkMetadata {

    override fun invoke(link: String): DeepLinkMetadata {
        val nsurl = NSURL(string = link)

        val components = NSURLComponents.componentsWithURL(
            url = nsurl,
            resolvingAgainstBaseURL = false,
        ) ?: return DeepLinkMetadata(
            scheme = null,
            host = null,
            path = "/",
            query = null,
        )

        return DeepLinkMetadata(
            scheme = components.scheme,
            host = components.host,
            path = components.path ?: "/",
            query = components.query,
        )
    }
}
