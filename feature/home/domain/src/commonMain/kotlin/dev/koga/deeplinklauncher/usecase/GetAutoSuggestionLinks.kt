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
            getDeepLinkMetadata.execute(it.link)
        }

        val linkMetadata = getDeepLinkMetadata.execute(link)

        val results = when {
            linkMetadata.scheme.isNullOrBlank() ->
                deepLinksMetadata.schemes(linkMetadata.link)

            linkMetadata.host.isNullOrBlank() ->
                deepLinksMetadata.hosts(linkMetadata.scheme)

            linkMetadata.query.isNullOrBlank() ->
                deepLinksMetadata.queries(linkMetadata.scheme, linkMetadata.host)

            else -> emptyList()
        }


        return results.distinct().take(n = MAX_RESULTS)
    }

    private fun List<DeepLinkMetadata>.schemes(text: String): List<String> {
        return filter { it.scheme != null && it.scheme.contains(text) }.map { "${it.scheme}:" }
    }

    private fun List<DeepLinkMetadata>.hosts(scheme: String): List<String> {
        return filter { it.scheme == scheme && it.host != null }.map { it.link }
    }

    private fun List<DeepLinkMetadata>.queries(scheme: String, host: String): List<String> {
        return filter {
            it.scheme == scheme && it.host == host && !it.query.isNullOrBlank()
        }.map { it.link }
    }

    companion object {
        private const val MAX_RESULTS = 4
    }
}
