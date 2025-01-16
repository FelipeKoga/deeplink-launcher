package dev.koga.deeplinklauncher.importexport.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.importexport.usecase.GetDeepLinksPlainTextPreview

internal class DefaultGetDeepLinksPlainTextPreview(
    private val repository: DeepLinkRepository,
) : GetDeepLinksPlainTextPreview {

    override operator fun invoke(): String {
        val deepLinks = repository.getDeepLinks()

        return deepLinks.joinToString(separator = "\n") { deepLink ->
            deepLink.link
        }
    }
}
