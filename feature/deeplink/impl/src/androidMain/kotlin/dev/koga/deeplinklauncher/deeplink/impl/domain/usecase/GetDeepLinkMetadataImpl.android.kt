package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import androidx.core.net.toUri
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkMetadata

internal class GetDeepLinkMetadataImpl : GetDeepLinkMetadata {
    override fun invoke(link: String): DeepLinkMetadata {
        val uri = link.toUri()

        return DeepLinkMetadata(
            scheme = uri.scheme,
            host = uri.host,
            path = uri.path ?: "/",
            query = uri.query,
        )
    }
}
