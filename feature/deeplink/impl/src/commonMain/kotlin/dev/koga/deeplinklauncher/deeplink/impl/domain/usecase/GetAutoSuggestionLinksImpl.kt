package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Suggestion
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetDeepLinkMetadata
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource

internal class GetAutoSuggestionLinksImpl(
    private val repository: DeepLinkRepository,
    private val getDeepLinkMetadata: GetDeepLinkMetadata,
    private val preferencesDataSource: PreferencesDataSource,
    private val getDeepLinkFromClipboard: GetDeepLinkFromClipboard,
) : GetAutoSuggestionLinks {

    private var cachedIndex: MetadataIndex? = null

    override operator fun invoke(link: String): List<Suggestion> {
        if (preferencesDataSource.preferences?.shouldDisableDeepLinkSuggestions == true) {
            return listOf()
        }

        val suggestions = getSuggestionsBasedOnHistory(link)
        if (link.isNotBlank()) return suggestions
        val deeplinkFromClipboard = getDeepLinkFromClipboard() ?: return suggestions

        return listOf(Suggestion.Clipboard(deeplinkFromClipboard)) + suggestions
    }

    private fun getSuggestionsBasedOnHistory(link: String): List<Suggestion> {
        val deepLinks = repository.getDeepLinks()
        val entries = metadataIndex(deepLinks).entries
        val occurrences = entries.groupingBy { it.metadata.scheme }.eachCount()
        val sortedEntries = entries.sortedByDescending { occurrences[it.metadata.scheme] }

        val linkMetadata = getDeepLinkMetadata(link)

        return when {
            linkMetadata.scheme.isNullOrBlank() ->
                sortedEntries.schemes(link)

            linkMetadata.host.isNullOrBlank() ->
                sortedEntries.hosts(linkMetadata.scheme!!)

            linkMetadata.query.isNullOrBlank() ->
                sortedEntries.queries(linkMetadata.scheme!!, linkMetadata.host!!)

            else -> emptyList()
        }.distinct().take(n = MAX_RESULTS)
    }

    private fun metadataIndex(deepLinks: List<DeepLink>): MetadataIndex {
        val signature = deepLinks.joinToString(separator = "|") { "${it.id}:${it.link}" }
        val cached = cachedIndex
        if (cached != null && cached.signature == signature) {
            return cached
        }

        return MetadataIndex(
            signature = signature,
            entries = deepLinks.map { deepLink ->
                MetadataEntry(
                    link = deepLink.link,
                    metadata = getDeepLinkMetadata(deepLink.link),
                )
            },
        ).also { cachedIndex = it }
    }

    private fun List<MetadataEntry>.schemes(text: String): List<Suggestion> {
        return filter { entry ->
            entry.metadata.scheme?.contains(text, ignoreCase = true) == true
        }.map {
            Suggestion.History(
                text = when {
                    it.link.contains("://") -> "${it.metadata.scheme}://"
                    else -> "${it.metadata.scheme}:"
                },
            )
        }
    }

    private fun List<MetadataEntry>.hosts(scheme: String): List<Suggestion> {
        return filter { it.metadata.scheme == scheme && it.metadata.host != null }
            .map { Suggestion.History(text = it.link) }
    }

    private fun List<MetadataEntry>.queries(scheme: String, host: String): List<Suggestion> {
        return filter {
            it.metadata.scheme == scheme && it.metadata.host == host && !it.metadata.query.isNullOrBlank()
        }.map { Suggestion.History(text = it.link) }
    }

    private data class MetadataIndex(
        val signature: String,
        val entries: List<MetadataEntry>,
    )

    private data class MetadataEntry(
        val link: String,
        val metadata: DeepLinkMetadata,
    )

    companion object {
        private const val MAX_RESULTS = 4
    }
}
