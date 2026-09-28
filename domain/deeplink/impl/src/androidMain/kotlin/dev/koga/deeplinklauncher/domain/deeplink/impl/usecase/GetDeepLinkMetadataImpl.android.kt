package dev.koga.deeplinklauncher.domain.deeplink.impl.usecase
import androidx.core.net.toUri
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkMetadata
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
