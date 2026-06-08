package dev.koga.deeplinklauncher.deeplink.uicomponent

import dev.koga.deeplinklauncher.deeplink.api.domain.model.Suggestion
import kotlinx.collections.immutable.persistentListOf

public data class DeepLinkInputState(
    val text: String = "",
    val errorMessage: String? = null,
    val suggestions: List<Suggestion> = persistentListOf(),
)
