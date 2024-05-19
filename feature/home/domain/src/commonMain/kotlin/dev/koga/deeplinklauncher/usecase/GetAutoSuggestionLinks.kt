package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource

class GetAutoSuggestionLinks(
    private val deepLinkDataSource: DeepLinkDataSource,
    private val getDeepLinkMetadata: GetDeepLinkMetadata,
) {
    fun execute(link: String): List<String> {
        val deepLinksMetadata = deepLinkDataSource.getDeepLinks().map {
            getDeepLinkMetadata.execute(it)
        }

        if (link.length <= 3) {
            return deepLinksMetadata
                .mapNotNull { it.scheme }
                .distinct()
        }

        return deepLinksMetadata
            .filter { it.deepLink.link.startsWith(link) && link != it.deepLink.link }
            .map { it.deepLink.link }
            .take(MAX_RESULTS)
    }

    companion object {
        private const val MAX_RESULTS = 6
    }
}
