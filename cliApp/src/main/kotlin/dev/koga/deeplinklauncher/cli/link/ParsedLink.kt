package dev.koga.deeplinklauncher.cli.link

internal class ParsedLink(raw: String) {

    val text: String = raw.trim()

    val isValid: Boolean = ':' in text

    val scheme: String?

    val host: String?

    val path: String

    val query: String?

    val fragment: String?

    val origin: String?

    val looksLikeDeepLink: Boolean

    init {
        val schemeEnd = text.indexOf(':').takeIf { it > 0 && text.isSchemeName(end = it) }
        val hierStart = schemeEnd?.plus(1) ?: 0
        val fragmentStart = text.indexOf('#', hierStart).takeIf { it >= 0 } ?: text.length
        val queryStart = text.indexOf('?', hierStart).takeIf { it in 0..<fragmentStart } ?: fragmentStart
        val authorityStart = (hierStart + 2).takeIf { text.startsWith("//", hierStart) }
        val authority = authorityStart?.let { start ->
            text.substring(start, text.indexOf('/', start).takeIf { it in 0..<queryStart } ?: queryStart)
        }
        val pathStart = authorityStart?.plus(authority.orEmpty().length) ?: hierStart
        val parsedScheme = schemeEnd?.let { text.substring(0, it) }

        scheme = parsedScheme
        host = authority?.substringAfterLast('@')?.withoutPort()?.ifBlank { null }
        path = text.substring(pathStart, queryStart).ifBlank { "" }
        query = if (queryStart < fragmentStart) text.substring(queryStart + 1, fragmentStart).ifBlank { null } else null
        fragment = if (fragmentStart < text.length) text.substring(fragmentStart + 1).ifBlank { null } else null
        origin = if (parsedScheme != null && authority != null) text.substring(0, pathStart) else null
        looksLikeDeepLink = parsedScheme != null &&
            parsedScheme.length >= 2 &&
            text.length > parsedScheme.length + 1 &&
            text.none { it.isWhitespace() }
    }

    override fun equals(other: Any?): Boolean = other is ParsedLink && other.text == text

    override fun hashCode(): Int = text.hashCode()

    override fun toString(): String = text
}

private fun String.isSchemeName(end: Int): Boolean =
    this[0].isAsciiLetter() && (1..<end).all { this[it].isSchemeChar() }

private fun Char.isAsciiLetter(): Boolean = this in 'a'..'z' || this in 'A'..'Z'

private fun Char.isSchemeChar(): Boolean =
    isAsciiLetter() || this in '0'..'9' || this == '+' || this == '-' || this == '.'

private fun String.withoutPort(): String {
    val lastNonDigit = indexOfLast { it !in '0'..'9' }
    return if (lastNonDigit >= 0 && this[lastNonDigit] == ':') substring(0, lastNonDigit) else this
}
