package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetAutoSuggestionLinks
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinkMetadata
import dev.koga.deeplinklauncher.preferences.api.repository.PreferencesRepository
import kotlinx.collections.immutable.persistentListOf

class GetAutoSuggestionLinksImpl(
    private val repository: DeepLinkRepository,
    private val getDeepLinkMetadata: GetDeepLinkMetadata,
    private val preferencesRepository: PreferencesRepository,
) : GetAutoSuggestionLinks {

    override operator fun invoke(link: String): List<String> {
        if (preferencesRepository.preferences.shouldDisableDeepLinkSuggestions) {
            return persistentListOf()
        }

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

    private fun List<DeepLinkMetadata>.schemes(text: String): List<String> {
        return filter { it.scheme != null && it.scheme!!.contains(text) }.map {
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
