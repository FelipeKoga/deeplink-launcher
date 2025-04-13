package dev.koga.deeplinklauncher.deeplink.api.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.Suggestion

interface GetAutoSuggestionLinks {
    operator fun invoke(link: String): List<Suggestion>
}
