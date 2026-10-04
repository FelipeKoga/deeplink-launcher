package dev.koga.deeplinklauncher.cli.command

import com.github.ajalt.clikt.testing.test
import dev.koga.deeplinklauncher.cli.FakeRunner
import dev.koga.deeplinklauncher.cli.androidEmulator
import dev.koga.deeplinklauncher.cli.androidOnly
import dev.koga.deeplinklauncher.cli.deeplinkCli
import dev.koga.deeplinklauncher.cli.fixture
import dev.koga.deeplinklauncher.cli.iosOnly
import dev.koga.deeplinklauncher.cli.process.CommandResult
import dev.koga.deeplinklauncher.cli.success
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OpenCommandTest {

    @Test
    fun reportsTheHandlerAndLaunchTimeWhenTheAppStaysAlive() {
        val runner = androidEmulator { command ->
            when {
                command.contains("am start -W") -> success(fixture("android/am-start-ok.txt"))
                command.contains("pidof") -> success("28085\n")
                else -> null
            }
        }

        val result = deeplinkCli { androidOnly(runner) }.test("open https://www.google.com/search?q=a&hl=en --json")
        val json = Json.parseToJsonElement(result.stdout).jsonObject

        assertEquals(0, result.statusCode)
        assertEquals("opened", json["status"]!!.jsonPrimitive.content)
        assertEquals("cold", json["launch"]!!.jsonPrimitive.content)
        assertEquals("4451", json["timeMs"]!!.jsonPrimitive.content)
        assertEquals(
            "com.android.chrome/org.chromium.chrome.browser.firstrun.FirstRunActivity",
            json["handler"]!!.jsonObject["id"]!!.jsonPrimitive.content,
        )
    }

    @Test
    fun sendsTheWholeLinkAsOneQuotedShellArgument() {
        val runner = androidEmulator { command -> if (command.contains("am start -W")) success(fixture("android/am-start-ok.txt")) else null }

        deeplinkCli { androidOnly(runner) }.test(listOf("open", "https://a.com/x?a=1&b=2"))

        val start = runner.calls.single { it.last().startsWith("am start") }
        assertEquals(listOf("adb", "-s", "emulator-5554", "shell"), start.dropLast(1))
        assertTrue(start.last().endsWith("-d 'https://a.com/x?a=1&b=2'"))
    }

    @Test
    fun reportsACrashWithTheCrashLogWhenTheAppDies() {
        val crash = """
            --------- beginning of crash
            10-04 18:00:01.000  4321  4321 E AndroidRuntime: FATAL EXCEPTION: main
            10-04 18:00:01.000  4321  4321 E AndroidRuntime: Process: com.android.chrome, PID: 4321
            10-04 18:00:01.000  4321  4321 E AndroidRuntime: java.lang.IllegalStateException: boom
        """.trimIndent()
        val runner = androidEmulator { command ->
            when {
                command.contains("am start -W") -> success(fixture("android/am-start-ok.txt"))
                command.contains("pidof") -> CommandResult(1, "", "")
                command.contains("logcat -b crash") -> success(crash)
                else -> null
            }
        }

        val result = deeplinkCli { androidOnly(runner) }.test("open myapp://x")

        assertEquals(1, result.statusCode)
        assertTrue(result.stdout.startsWith("✗ Crashed on Medium_Phone_API_35"))
        assertTrue(result.stdout.contains("IllegalStateException: boom"))
    }

    @Test
    fun failsWhenNoAndroidAppHandlesTheLink() {
        val runner = androidEmulator { command ->
            if (command.contains("am start -W")) CommandResult(1, fixture("android/am-start-unresolved.txt"), "") else null
        }

        val result = deeplinkCli { androidOnly(runner) }.test("open nosuchscheme://x --json")

        assertEquals(1, result.statusCode)
        assertEquals("unhandled", Json.parseToJsonElement(result.stdout).jsonObject["status"]!!.jsonPrimitive.content)
    }

    @Test
    fun failsWhenNoIosAppHandlesTheLink() {
        val runner = FakeRunner { command ->
            when {
                command.contains("simctl list") -> success(fixture("ios/simctl-list-devices.json"))
                command.contains("simctl openurl") -> CommandResult(194, "", fixture("ios/openurl-unhandled.stderr.txt"))
                else -> success("{}")
            }
        }

        val result = deeplinkCli { iosOnly(runner) }.test("open nosuchscheme://x")

        assertEquals(1, result.statusCode)
        assertTrue(result.stdout.contains("No installed app handles this link."))
    }

    @Test
    fun printsAJsonErrorWhenSeveralDevicesAreRunning() {
        val runner = FakeRunner { command ->
            when {
                command == "adb devices -l" -> success("List of devices attached\nemulator-5554 device model:a\nemulator-5556 device model:b\n")
                else -> success()
            }
        }

        val result = deeplinkCli { androidOnly(runner) }.test("open myapp://x --json")
        val error = Json.parseToJsonElement(result.stdout).jsonObject["error"]!!.jsonObject

        assertEquals(3, result.statusCode)
        assertEquals("3", error["exitCode"]!!.jsonPrimitive.content)
        assertTrue(error["hint"]!!.jsonPrimitive.content.contains("--device emulator-5556"))
    }
}
