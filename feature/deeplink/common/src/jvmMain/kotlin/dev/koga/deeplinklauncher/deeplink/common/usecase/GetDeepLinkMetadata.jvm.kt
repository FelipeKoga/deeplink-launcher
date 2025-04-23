package dev.koga.deeplinklauncher.deeplink.common.usecase

import dev.koga.deeplinklauncher.deeplink.common.model.DeepLinkMetadata

internal class GetDeepLinkMetadataImpl : GetDeepLinkMetadata {
    override fun invoke(link: String): DeepLinkMetadata {
        val scheme = link.substringBefore(':', "").takeIf { it.isNotBlank() }

        val remainingLink = link.substringAfter(':', "")

        val host = if (remainingLink.startsWith("//")) {
            remainingLink.substringAfter("//").substringBefore("?")
        } else {
            remainingLink.substringBefore("?")
        }

        val query = remainingLink.substringAfter("?", "").takeIf { it.isNotBlank() }

        return DeepLinkMetadata(
            link = link,
            scheme = scheme,
            host = host.ifEmpty { null },
            query = query,
        )
    }
}