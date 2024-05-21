package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.PreferencesDataSource

class GetAutoSuggestionLinks(
    private val deepLinkDataSource: DeepLinkDataSource,
    private val getDeepLinkMetadata: GetDeepLinkMetadata,
    private val preferencesDataSource: PreferencesDataSource,
) {
    fun execute(link: String): List<String> {
        if (preferencesDataSource.preferences.shouldDisableDeepLinkSuggestions) {
            return emptyList()
        }

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
        private const val MAX_RESULTS = 4
    }
}
