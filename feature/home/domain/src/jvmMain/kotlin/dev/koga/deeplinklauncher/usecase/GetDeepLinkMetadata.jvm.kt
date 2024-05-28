package dev.koga.deeplinklauncher.usecase

actual class GetDeepLinkMetadata {
    actual fun execute(link: String): DeepLinkMetadata {
        return DeepLinkMetadata(
            link = "autem", scheme = null, host = null, query = null
        )
    }

}