package dev.koga.deeplinklauncher.deeplink.api.link

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ParsedLinkTest {

    @Test
    fun tableRowsMatch() {
        rows.forEach { (raw, expected) ->
            assertEquals(expected, ParsedLink(raw).parts(), "raw=[$raw]")
        }
    }

    @Test
    fun invariantsHoldOnShortStrings() {
        (shortStrings() + edgeStrings).forEach { raw -> assertInvariants(raw) }
    }

    @Test
    fun heuristicMatchesClipboardRegex() {
        val clipboardPattern = Regex("^[A-Za-z][A-Za-z0-9+.-]+:\\S+$")
        shortStrings().forEach { raw ->
            assertEquals(clipboardPattern.matches(raw.trim()), ParsedLink(raw).looksLikeDeepLink, "raw=[$raw]")
        }
    }

    @Test
    fun equalityFollowsTrimmedText() {
        assertEquals(ParsedLink("myapp://x"), ParsedLink("  myapp://x\n"))
        assertEquals("myapp://x".hashCode(), ParsedLink(" myapp://x ").hashCode())
        assertEquals("myapp://x", ParsedLink("\tmyapp://x").toString())
        assertNotEquals(ParsedLink("myapp://x"), ParsedLink("MyApp://x"))
    }

    private fun assertInvariants(raw: String) {
        val link = ParsedLink(raw)
        val message = "raw=[$raw]"
        assertEquals(raw.trim(), link.text, message)
        assertEquals(':' in raw, link.isValid, message)
        assertTrue(!link.looksLikeDeepLink || link.isValid, message)
        listOfNotNull(link.scheme, link.host, link.query, link.fragment, link.origin).forEach { part ->
            assertTrue(part.isNotBlank() && part in link.text, message)
        }
        assertTrue(link.path in link.text && '?' !in link.path && '#' !in link.path, message)
        assertTrue(link.path.isEmpty() || link.path.isNotBlank(), message)
        assertTrue(link.host?.any { it in "@/?#" } != true, message)
        assertTrue(link.query?.contains('#') != true, message)
        assertTrue(link.origin?.let { link.text.startsWith(it) } != false, message)
        assertTrue(link.scheme?.let { link.text.startsWith("$it:") } != false, message)
    }

    private fun shortStrings(): Sequence<String> = sequence {
        var level = listOf("")
        yieldAll(level)
        repeat(SHORT_STRING_MAX_LENGTH) {
            level = level.flatMap { prefix -> SHORT_STRING_ALPHABET.map { prefix + it } }
            yieldAll(level)
        }
    }

    private data class Parts(
        val text: String,
        val isValid: Boolean,
        val looksLikeDeepLink: Boolean,
        val scheme: String?,
        val host: String?,
        val path: String,
        val query: String?,
        val fragment: String?,
        val origin: String?,
    )

    private fun ParsedLink.parts() = Parts(text, isValid, looksLikeDeepLink, scheme, host, path, query, fragment, origin)

    private companion object {
        const val SHORT_STRING_MAX_LENGTH = 4
        const val SHORT_STRING_ALPHABET = "aZ1:/?#@[]% +-."

        val edgeStrings = listOf(
            "\uD800",
            "\u0000",
            "myapp://\uD83D\uDE00",
            "a".repeat(100_000) + ":",
            "myapp://" + "x".repeat(100_000),
        )

        val rows = listOf(
            "myapp://" to Parts("myapp://", true, true, "myapp", null, "", null, null, "myapp://"),
            "myapp://a b" to Parts("myapp://a b", true, false, "myapp", "a b", "", null, null, "myapp://a b"),
            "myapp:home" to Parts("myapp:home", true, true, "myapp", null, "home", null, null, null),
            "mailto:a@b.com" to Parts("mailto:a@b.com", true, true, "mailto", null, "a@b.com", null, null, null),
            "a:b" to Parts("a:b", true, false, "a", null, "b", null, null, null),
            "Note: buy milk" to Parts("Note: buy milk", true, false, "Note", null, " buy milk", null, null, null),
            "C:\\Users\\koga" to Parts("C:\\Users\\koga", true, false, "C", null, "\\Users\\koga", null, null, null),
            ":foo" to Parts(":foo", true, false, null, null, ":foo", null, null, null),
            "https://example.com/?q=a b" to Parts(
                "https://example.com/?q=a b", true, false, "https", "example.com", "/", "q=a b", null,
                "https://example.com",
            ),
            " myapp://x" to Parts("myapp://x", true, true, "myapp", "x", "", null, null, "myapp://x"),
            "myapp://open?data={\"id\":1}" to Parts(
                "myapp://open?data={\"id\":1}", true, true, "myapp", "open", "", "data={\"id\":1}", null,
                "myapp://open",
            ),
            "/path#frag" to Parts("/path#frag", false, false, null, null, "/path", null, "frag", null),
            "myapp://h:abc" to Parts("myapp://h:abc", true, true, "myapp", "h:abc", "", null, null, "myapp://h:abc"),
            "myapp://x?q=50%" to Parts("myapp://x?q=50%", true, true, "myapp", "x", "", "q=50%", null, "myapp://x"),
            "myapp://checkout/cart?id=1#top" to Parts(
                "myapp://checkout/cart?id=1#top", true, true, "myapp", "checkout", "/cart", "id=1", "top",
                "myapp://checkout",
            ),
            "" to Parts("", false, false, null, null, "", null, null, null),
            "MyApp://Host/Path" to Parts("MyApp://Host/Path", true, true, "MyApp", "Host", "/Path", null, null, "MyApp://Host"),
            "file:///x" to Parts("file:///x", true, true, "file", null, "/x", null, null, "file://"),
            "myapp://u@[::1]:80/p" to Parts(
                "myapp://u@[::1]:80/p", true, true, "myapp", "[::1]", "/p", null, null, "myapp://u@[::1]:80",
            ),
            "my app://x" to Parts("my app://x", true, false, null, null, "my app://x", null, null, null),
            "myapp://[x" to Parts("myapp://[x", true, true, "myapp", "[x", "", null, null, "myapp://[x"),
            "://x" to Parts("://x", true, false, null, null, "://x", null, null, null),
            "myapp://h%zz" to Parts("myapp://h%zz", true, true, "myapp", "h%zz", "", null, null, "myapp://h%zz"),
            "myapp://h%" to Parts("myapp://h%", true, true, "myapp", "h%", "", null, null, "myapp://h%"),
            "myapp:" to Parts("myapp:", true, false, "myapp", null, "", null, null, null),
            "myapp:///settings" to Parts("myapp:///settings", true, true, "myapp", null, "/settings", null, null, "myapp://"),
            "https://example.com/" to Parts(
                "https://example.com/", true, true, "https", "example.com", "/", null, null, "https://example.com",
            ),
            "myapp:x?next=https://y" to Parts(
                "myapp:x?next=https://y", true, true, "myapp", null, "x", "next=https://y", null, null,
            ),
            "myapp://h\\p/x" to Parts("myapp://h\\p/x", true, true, "myapp", "h\\p", "/x", null, null, "myapp://h\\p"),
            "my app:x" to Parts("my app:x", true, false, null, null, "my app:x", null, null, null),
            "/p:x" to Parts("/p:x", true, false, null, null, "/p:x", null, null, null),
            "myapp://h:" to Parts("myapp://h:", true, true, "myapp", "h", "", null, null, "myapp://h:"),
            "myapp://user@host:12/p" to Parts(
                "myapp://user@host:12/p", true, true, "myapp", "host", "/p", null, null, "myapp://user@host:12",
            ),
            "//example.com/x" to Parts("//example.com/x", false, false, null, "example.com", "/x", null, null, null),
            "myapp://x#a?b" to Parts("myapp://x#a?b", true, true, "myapp", "x", "", null, "a?b", "myapp://x"),
            "myapp://x?a#b?c" to Parts("myapp://x?a#b?c", true, true, "myapp", "x", "", "a", "b?c", "myapp://x"),
            "myapp://x? #" to Parts("myapp://x? #", true, false, "myapp", "x", "", null, null, "myapp://x"),
            "myapp://h?x/y" to Parts("myapp://h?x/y", true, true, "myapp", "h", "", "x/y", null, "myapp://h"),
            "myapp://u@v@h:1/p" to Parts("myapp://u@v@h:1/p", true, true, "myapp", "h", "/p", null, null, "myapp://u@v@h:1"),
            "myapp://shop/items/42" to Parts(
                "myapp://shop/items/42", true, true, "myapp", "shop", "/items/42", null, null, "myapp://shop",
            ),
            "com.example-app+dev://home/p" to Parts(
                "com.example-app+dev://home/p", true, true, "com.example-app+dev", "home", "/p", null, null,
                "com.example-app+dev://home",
            ),
            "myapp://a\tb" to Parts("myapp://a\tb", true, false, "myapp", "a\tb", "", null, null, "myapp://a\tb"),
            "ab:c\u001Fd" to Parts("ab:c\u001Fd", true, false, "ab", null, "c\u001Fd", null, null, null),
            "café://menu" to Parts("café://menu", true, false, null, null, "café://menu", null, null, null),
            "myapp://café" to Parts("myapp://café", true, true, "myapp", "café", "", null, null, "myapp://café"),
            "myapp://h:\uFF18\uFF10" to Parts(
                "myapp://h:\uFF18\uFF10", true, true, "myapp", "h:\uFF18\uFF10", "", null, null, "myapp://h:\uFF18\uFF10",
            ),
            "myapp: ?q" to Parts("myapp: ?q", true, false, "myapp", null, "", "q", null, null),
            "myapp://x?ids=1|2" to Parts("myapp://x?ids=1|2", true, true, "myapp", "x", "", "ids=1|2", null, "myapp://x"),
            "myapp://h/a%20b?q=%7B" to Parts("myapp://h/a%20b?q=%7B", true, true, "myapp", "h", "/a%20b", "q=%7B", null, "myapp://h"),
        )
    }
}
