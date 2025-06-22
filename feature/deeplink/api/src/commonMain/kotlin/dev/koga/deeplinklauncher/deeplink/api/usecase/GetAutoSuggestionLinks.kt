package dev.koga.deeplinklauncher.deeplink.api.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.Suggestion

public interface GetAutoSuggestionLinks {
    public operator fun invoke(link: String): List<Suggestion>
}
