package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.repository.DeepLinkRepository

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