package dev.koga.deeplinklauncher.usecase

actual class GetDeepLinkMetadata {
    actual fun execute(link: String): DeepLinkMetadata {
        val scheme = link.substringBefore(':', "").takeIf { it.isNotBlank() }

        val remainingLink = link.substringAfter(':', "")

        val host = if (remainingLink.startsWith("//")) {
            remainingLink.substringAfter("//").substringBefore("?")
        } else {
            remainingLink.substringBefore("?") // para casos como mailto ou sms
        }

        val query = remainingLink.substringAfter("?", "").takeIf { it.isNotBlank() }

        return DeepLinkMetadata(
            link = link,
            scheme = scheme,
            host = host.ifEmpty { null },
            query = query
        )
    }
}
