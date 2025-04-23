package dev.koga.deeplinklauncher.deeplink.common.usecase

import android.net.Uri
import dev.koga.deeplinklauncher.deeplink.common.model.DeepLinkMetadata

internal class GetDeepLinkMetadataImpl : GetDeepLinkMetadata {
    override fun invoke(link: String): DeepLinkMetadata {
        val uri = Uri.parse(link)

        return DeepLinkMetadata(
            link = link,
            scheme = uri.scheme,
            query = uri.query,
            host = uri.host,
        )
    }
}
