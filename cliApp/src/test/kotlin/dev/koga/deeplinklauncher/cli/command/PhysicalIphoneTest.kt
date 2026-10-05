package dev.koga.deeplinklauncher.cli.command

import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.testing.test
import dev.koga.deeplinklauncher.cli.DeeplinkCommand
import dev.koga.deeplinklauncher.cli.deeplinkCli
import dev.koga.deeplinklauncher.cli.fixture
import dev.koga.deeplinklauncher.cli.physicalIphone
import dev.koga.deeplinklauncher.cli.physicalOnly
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PhysicalIphoneTest {

    private fun iphone(processes: String = fixture("ios/devicectl-processes.json"), launch: String = fixture("ios/devicectl-launch-ok.json")) =
        physicalIphone { command ->
            when {
                command.contains("list devices") -> fixture("ios/devicectl-list-devices.json")
                command.contains("process launch") -> launch
                command.contains("info processes") -> processes
                else -> null
            }
        }

    @Test
    fun opensTheLinkInsideTheGivenAppAndChecksItStaysAlive() {
        val runner = iphone()

        val result = deeplinkCli { physicalOnly(runner) }.test(listOf("open", "myapp://cart?id=1&ref=mail", "--app", "com.acme", "--json"))
        val json = Json.parseToJsonElement(result.stdout).jsonObject

        assertEquals(0, result.statusCode)
        assertEquals("opened", json["status"]!!.jsonPrimitive.content)
        assertEquals("com.acme", json["handler"]!!.jsonObject["id"]!!.jsonPrimitive.content)
        val launch = runner.calls.single { "launch" in it }
        assertEquals(
            listOf("--device", "00008130-000A1234ABCD001C", "--terminate-existing", "--payload-url", "myapp://cart?id=1&ref=mail", "com.acme"),
            launch.dropWhile { it != "--device" }.takeWhile { it != "--json-output" },
        )
    }

    @Test
    fun reportsACrashWhenTheLaunchedProcessIsGone() {
        val gone = fixture("ios/devicectl-processes.json").replace("\"processIdentifier\" : 1234", "\"processIdentifier\" : 4321")

        val result = deeplinkCli { physicalOnly(iphone(processes = gone)) }.test("open myapp://cart --app com.acme")

        assertEquals(1, result.statusCode)
        assertTrue(result.stdout.startsWith("✗ Crashed on Koga's iPhone"))
    }

    @Test
    fun reportsWhyDevicectlCouldNotLaunchTheApp() {
        val result = deeplinkCli { physicalOnly(iphone(launch = fixture("ios/devicectl-launch-failed.json"))) }
            .test("open myapp://cart --app com.acme.missing")

        assertEquals(1, result.statusCode)
        assertTrue(result.stdout.contains("The requested application com.acme.missing is not installed."))
    }

    @Test
    fun asksForTheAppBeforeOpeningAnything() {
        val runner = iphone()

        val result = deeplinkCli { physicalOnly(runner) }.test("open myapp://cart")

        assertEquals(2, result.statusCode)
        assertTrue(result.stderr.contains("pass its bundle id with --app"))
        assertTrue(runner.calls.none { "launch" in it })
    }

    @Test
    fun refusesASuiteWithLinksThatHaveNoTargetApp() {
        val runner = iphone()
        val suite = """{ "deepLinks": [
            { "link": "myapp://cart", "name": "Cart", "expect": { "ios": "com.acme" } },
            { "link": "myapp://help", "name": "Help" } ] }"""

        val result = DeeplinkCommand().subcommands(TestCommand({ physicalOnly(runner) }, readStdin = { suite })).test("test -")

        assertEquals(2, result.statusCode)
        assertTrue(result.stderr.contains("on: Help"))
        assertTrue(runner.calls.none { "launch" in it })
    }

    @Test
    fun runsASuiteWithTheDefaultApp() {
        val result = DeeplinkCommand()
            .subcommands(TestCommand({ physicalOnly(iphone()) }, readStdin = { "myapp://cart\nmyapp://help\n" }))
            .test("test - --app com.acme")

        assertEquals(0, result.statusCode)
        assertTrue(result.stdout.contains("2 passed, 0 failed"))
    }
}
