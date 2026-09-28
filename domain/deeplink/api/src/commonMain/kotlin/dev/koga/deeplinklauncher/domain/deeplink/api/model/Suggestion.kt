package dev.koga.deeplinklauncher.domain.deeplink.api.model
public sealed interface Suggestion {
    public val text: String

    public data class Clipboard(override val text: String) : Suggestion
    public data class History(override val text: String) : Suggestion
}
