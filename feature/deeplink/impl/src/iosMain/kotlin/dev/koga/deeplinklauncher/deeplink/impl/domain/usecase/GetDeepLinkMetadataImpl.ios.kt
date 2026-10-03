package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkMetadata
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class GetDeepLinkMetadataImpl : GetDeepLinkMetadata {

    override fun invoke(link: String): DeepLinkMetadata {
        val components = NSURL.URLWithString(link)?.let { nsurl ->
            NSURLComponents.componentsWithURL(
                url = nsurl,
                resolvingAgainstBaseURL = false,
            )
        } ?: return DeepLinkMetadata(
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
