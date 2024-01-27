package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource

class GetDeepLinksPlainTextPreview(
    private val dataSource: DeepLinkDataSource
) {
    operator fun invoke(): String {
        val deepLinks = dataSource.getDeepLinks()

        return deepLinks.joinToString(separator = "\n") { deepLink ->
            deepLink.link
        }
    }
}