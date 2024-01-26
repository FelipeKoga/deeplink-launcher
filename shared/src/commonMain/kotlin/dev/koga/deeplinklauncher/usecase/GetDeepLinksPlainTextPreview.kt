package dev.koga.deeplinklauncher.usecase

class GetDeepLinksPlainTextPreview(
    private val repository: DeepLinkRepository
) {
    operator fun invoke(): String {
        val deepLinks = repository.getAllDeepLinks()

        return deepLinks.joinToString(separator = "\n") { deepLink ->
            deepLink.link
        }
    }
}