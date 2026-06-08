package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.Suggestion

public interface GetAutoSuggestionLinks {
    public operator fun invoke(link: String): List<Suggestion>
}
