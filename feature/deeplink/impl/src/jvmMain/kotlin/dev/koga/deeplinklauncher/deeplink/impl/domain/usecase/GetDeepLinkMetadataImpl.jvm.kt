package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkMetadata

internal class GetDeepLinkMetadataImpl : GetDeepLinkMetadata {

    override fun invoke(link: String): DeepLinkMetadata {
        val scheme = link.substringBefore(':', "").takeIf { it.isNotBlank() }

        val remainingLink = link.substringAfter(':', "")

        val host: String?
        val path: String

        if (remainingLink.startsWith("//")) {
            val afterAuthority = remainingLink.substringAfter("//")
            val pathStart = afterAuthority.indexOf('/')
            if (pathStart == -1) {
                host = afterAuthority.substringBefore("?").ifEmpty { null }
                path = "/"
            } else {
                host = afterAuthority.substring(0, pathStart).ifEmpty { null }
                path = afterAuthority.substring(pathStart).substringBefore("?").ifEmpty { "/" }
            }
        } else {
            host = remainingLink.substringBefore("?").ifEmpty { null }
            path = "/"
        }

        val query = remainingLink.substringAfter("?", "").takeIf { it.isNotBlank() }

        return DeepLinkMetadata(
            scheme = scheme,
            host = host,
            path = path,
            query = query,
        )
    }
}
