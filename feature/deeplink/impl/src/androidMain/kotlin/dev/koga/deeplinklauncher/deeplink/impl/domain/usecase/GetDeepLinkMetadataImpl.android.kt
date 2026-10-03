package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import androidx.core.net.toUri
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkMetadata
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
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
