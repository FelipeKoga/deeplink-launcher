package dev.koga.deeplinklauncher.cli.command

import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.testing.test
import dev.koga.deeplinklauncher.cli.DeeplinkCommand
import dev.koga.deeplinklauncher.cli.androidEmulator
import dev.koga.deeplinklauncher.cli.androidOnly
import dev.koga.deeplinklauncher.cli.fixture
import dev.koga.deeplinklauncher.cli.process.CommandResult
import dev.koga.deeplinklauncher.cli.success
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestCommandTest {

    private val runner = androidEmulator { command ->
        when {
            command.contains("am start -W") && command.contains("nosuchscheme") ->
                CommandResult(1, fixture("android/am-start-unresolved.txt"), "")
            command.contains("am start -W") -> success(fixture("android/am-start-ok.txt"))
            command.contains("pidof") -> success("28085\n")
            else -> null
        }
    }

    private fun cli(stdin: String) = DeeplinkCommand().subcommands(TestCommand({ androidOnly(runner) }, readStdin = { stdin }))

    @Test
    fun passesWhenEveryLinkMeetsItsExpectation() {
        val suite = """{ "deepLinks": [
            { "link": "https://google.com", "name": "Web", "targetPackage": "com.android.chrome" },
            { "link": "nosuchscheme://x", "name": "Blocked", "expect": { "opens": false } } ] }"""

        val result = cli(suite).test("test - --json")
        val json = Json.parseToJsonElement(result.stdout).jsonObject

        assertEquals(0, result.statusCode)
        assertEquals(2, json["summary"]!!.jsonObject["passed"]!!.jsonPrimitive.int)
        assertEquals(
            listOf("passed", "passed"),
            json["results"]!!.jsonArray.map { it.jsonObject["status"]!!.jsonPrimitive.content },
        )
    }

    @Test
    fun failsAndExplainsWhenALinkOpensTheWrongApp() {
        val result = cli("""{ "deepLinks": [ { "link": "myapp://x", "name": "Product", "targetPackage": "com.acme" } ] }""")
            .test("test -")

        assertEquals(1, result.statusCode)
        assertTrue(result.stdout.contains("✗ Product   expected com.acme, opened com.android.chrome/"))
        assertTrue(result.stdout.contains("0 passed, 1 failed"))
    }

    @Test
    fun runsAPlainListOfLinks() {
        val result = cli("myapp://a\nmyapp://b\n").test("test -")

        assertEquals(0, result.statusCode)
        assertTrue(result.stdout.startsWith("Ran 2 links on Medium_Phone_API_35"))
    }

    @Test
    fun rejectsAMissingFolderWithTheFoldersThatExist() {
        val suite = """{ "folders": [{ "id": "f", "name": "Checkout" }], "deepLinks": [ { "link": "myapp://x", "folderId": "f" } ] }"""

        val result = cli(suite).test("test - --folder login")

        assertEquals(2, result.statusCode)
        assertTrue(result.stderr.contains("Folders in the suite: Checkout"))
        assertEquals(emptyList(), runner.calls)
    }

    @Test
    fun rejectsAMissingSuiteFile() {
        val result = cli("").test("test /nope/links.json")

        assertEquals(2, result.statusCode)
        assertTrue(result.stderr.contains("Suite file not found: /nope/links.json"))
    }
}
