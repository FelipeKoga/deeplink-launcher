package dev.koga.deeplinklauncher.usecase

actual class GetDeepLinkMetadata {
    actual fun execute(link: String): DeepLinkMetadata {
        val scheme = link.substringBefore("://", "")
        val host = link.substringAfter("://", "")
        val query = host.substringAfter("?", "")

        return DeepLinkMetadata(
            link = link,
            scheme = scheme.ifEmpty { null },
            query = query.ifEmpty { null },
            host = host.ifEmpty { null },
        )
    }
}
