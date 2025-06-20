package dev.koga.deeplinklauncher.deeplink.api.model

sealed interface Suggestion {
    val text: String

    data class Clipboard(override val text: String) : Suggestion
    data class History(override val text: String) : Suggestion
}
