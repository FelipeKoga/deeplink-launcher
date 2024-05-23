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

        val deepLinks = deepLinkDataSource.getDeepLinks()

        val deepLinksMetadata = deepLinks.map { getDeepLinkMetadata.execute(it.link) }
        val occurrences = deepLinksMetadata.groupingBy { it.scheme }.eachCount()
        val sortedMetadata = deepLinksMetadata.sortedByDescending { occurrences[it.scheme] }

        val linkMetadata = getDeepLinkMetadata.execute(link)

        return when {
            linkMetadata.scheme.isNullOrBlank() ->
                sortedMetadata.schemes(linkMetadata.link)

            linkMetadata.host.isNullOrBlank() ->
                sortedMetadata.hosts(linkMetadata.scheme)

            linkMetadata.query.isNullOrBlank() ->
                sortedMetadata.queries(linkMetadata.scheme, linkMetadata.host)

            else -> emptyList()
        }.distinct().take(n = MAX_RESULTS)
    }

    private fun List<DeepLinkMetadata>.schemes(text: String): List<String> {
        return filter { it.scheme != null && it.scheme.contains(text) }.map {
            when {
                it.link.contains("://") -> "${it.scheme}://"
                else -> "${it.scheme}:"
            }
        }
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
