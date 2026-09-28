package dev.koga.deeplinklauncher.deeplink.ui
import dev.koga.deeplinklauncher.domain.deeplink.api.model.Suggestion
import kotlinx.collections.immutable.persistentListOf

public data class DeepLinkInputState(
    val text: String = "",
    val errorMessage: String? = null,
    val suggestions: List<Suggestion> = persistentListOf(),
)
