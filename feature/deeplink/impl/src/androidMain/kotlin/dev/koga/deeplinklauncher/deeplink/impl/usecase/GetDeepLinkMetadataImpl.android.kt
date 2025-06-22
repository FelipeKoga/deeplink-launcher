package dev.koga.deeplinklauncher.deeplink.impl.usecase

import androidx.core.net.toUri
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinkMetadata

internal class GetDeepLinkMetadataImpl : GetDeepLinkMetadata {
    override fun invoke(link: String): DeepLinkMetadata {
        val uri = link.toUri()

        return DeepLinkMetadata(
            link = link,
            scheme = uri.scheme,
            query = uri.query,
            host = uri.host,
        )
    }
}
