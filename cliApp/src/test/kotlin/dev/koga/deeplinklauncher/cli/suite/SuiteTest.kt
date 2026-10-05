package dev.koga.deeplinklauncher.cli.suite

import dev.koga.deeplinklauncher.cli.CliFailure
import dev.koga.deeplinklauncher.cli.ExitCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SuiteTest {

    @Test
    fun readsAnAppExportWithFoldersAndExpectations() {
        val cases = Suite.parse(
            """
            {
              "folders": [{ "id": "f1", "name": "Checkout", "description": "ignored" }],
              "deepLinks": [
                { "link": "myapp://cart", "name": "Cart", "folderId": "f1", "targetPackage": "com.acme", "isFavorite": true },
                { "link": "myapp://old", "expect": { "opens": false } },
                { "link": "myapp://both", "targetPackage": "com.acme", "expect": { "android": "com.acme/.Both", "ios": "com.acme.ios" } },
              ]
            }
            """.trimIndent(),
        )

        assertEquals(
            listOf(
                SuiteCase("Cart", "myapp://cart", "Checkout", Expectation(android = "com.acme")),
                SuiteCase(null, "myapp://old", null, Expectation(opens = false)),
                SuiteCase(null, "myapp://both", null, Expectation(android = "com.acme/.Both", ios = "com.acme.ios")),
            ),
            cases,
        )
    }

    @Test
    fun readsOneLinkPerLineAndSkipsCommentsAndBlankLines() {
        assertEquals(
            listOf("myapp://a", "https://b.com/x?y=1&z=2"),
            Suite.parse("myapp://a\n\n# login links\n  https://b.com/x?y=1&z=2  \n").map(SuiteCase::url),
        )
    }

    @Test
    fun rejectsAnExportThatIsNotValidJsonAsAUsageError() {
        val failure = assertFailsWith<CliFailure> { Suite.parse("{ \"deepLinks\": [ { \"name\": \"no link\" } ] }") }

        assertEquals(ExitCode.USAGE, failure.exitCode)
    }
}
