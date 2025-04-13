package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.model.Suggestion
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinkMetadata
import dev.koga.deeplinklauncher.preferences.api.repository.PreferencesRepository

class GetAutoSuggestionLinksImpl(
    private val repository: DeepLinkRepository,
    private val getDeepLinkMetadata: GetDeepLinkMetadata,
    private val preferencesRepository: PreferencesRepository,
    private val getDeepLinkFromClipboard: GetDeepLinkFromClipboard,
) : GetAutoSuggestionLinks {

    override operator fun invoke(link: String): List<Suggestion> {
        if (preferencesRepository.preferences.shouldDisableDeepLinkSuggestions) {
            return listOf()
        }

        val suggestions = getSuggestionsBasedOnHistory(link)
        if (link.isNotBlank()) return suggestions
        val deeplinkFromClipboard = getDeepLinkFromClipboard() ?: return suggestions

        return listOf(Suggestion.Clipboard(deeplinkFromClipboard)) + suggestions
    }

    private fun getSuggestionsBasedOnHistory(link: String): List<Suggestion> {
        val deepLinks = repository.getDeepLinks()

        val deepLinksMetadata = deepLinks.map { getDeepLinkMetadata(it.link) }
        val occurrences = deepLinksMetadata.groupingBy { it.scheme }.eachCount()
        val sortedMetadata = deepLinksMetadata.sortedByDescending { occurrences[it.scheme] }

        val linkMetadata = getDeepLinkMetadata(link)

        return when {
            linkMetadata.scheme.isNullOrBlank() ->
                sortedMetadata.schemes(linkMetadata.link)

            linkMetadata.host.isNullOrBlank() ->
                sortedMetadata.hosts(linkMetadata.scheme!!)

            linkMetadata.query.isNullOrBlank() ->
                sortedMetadata.queries(linkMetadata.scheme!!, linkMetadata.host!!)

            else -> emptyList()
        }.distinct().take(n = MAX_RESULTS)
    }

    private fun List<DeepLinkMetadata>.schemes(text: String): List<Suggestion> {
        return filter { it.scheme != null && it.scheme!!.contains(text) }.map {
            Suggestion.History(
                text = when {
                    it.link.contains("://") -> "${it.scheme}://"
                    else -> "${it.scheme}:"
                },
            )
        }
    }

    private fun List<DeepLinkMetadata>.hosts(scheme: String): List<Suggestion> {
        return filter { it.scheme == scheme && it.host != null }
            .map { Suggestion.History(text = it.link) }
    }

    private fun List<DeepLinkMetadata>.queries(scheme: String, host: String): List<Suggestion> {
        return filter {
            it.scheme == scheme && it.host == host && !it.query.isNullOrBlank()
        }.map { Suggestion.History(text = it.link) }
    }

    companion object {
        private const val MAX_RESULTS = 4
    }
}
