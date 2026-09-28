package dev.koga.deeplinklauncher.domain.deeplink.api.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.model.Suggestion
public interface GetAutoSuggestionLinks {
    public suspend operator fun invoke(link: String): List<Suggestion>
}
