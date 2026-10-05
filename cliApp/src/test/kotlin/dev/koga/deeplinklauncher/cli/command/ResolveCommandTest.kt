package dev.koga.deeplinklauncher.cli.command

import com.github.ajalt.clikt.testing.test
import dev.koga.deeplinklauncher.cli.FakeRunner
import dev.koga.deeplinklauncher.cli.Toolchain
import dev.koga.deeplinklauncher.cli.androidEmulator
import dev.koga.deeplinklauncher.cli.androidOnly
import dev.koga.deeplinklauncher.cli.deeplinkCli
import dev.koga.deeplinklauncher.cli.fixture
import dev.koga.deeplinklauncher.cli.iosOnly
import dev.koga.deeplinklauncher.cli.success
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ResolveCommandTest {

    @Test
    fun listsEveryAndroidHandlerAndTheDefault() {
        val runner = androidEmulator { command ->
            when {
                command.contains("query-activities") -> success(fixture("android/query-many.txt"))
                command.contains("resolve-activity") -> success(fixture("android/resolve-many.txt"))
                else -> null
            }
        }

        val result = deeplinkCli { androidOnly(runner) }.test("resolve tel:123")

        assertEquals(0, result.statusCode)
        assertTrue(result.stdout.startsWith("✓ 2 apps can open tel:123"))
        assertTrue(result.stdout.contains("default    com.google.android.dialer/com.android.dialer.main.impl.MainActivity"))
    }

    @Test
    fun notesWhenAndroidWouldShowTheChooser() {
        val runner = androidEmulator { command ->
            when {
                command.contains("query-activities") -> success(fixture("android/query-many.txt"))
                command.contains("resolve-activity") -> success("android/com.android.internal.app.ResolverActivity\n")
                else -> null
            }
        }

        val result = deeplinkCli { androidOnly(runner) }.test("resolve tel:123")

        assertTrue(result.stdout.contains("No default app: Android will ask the user to choose."))
    }

    @Test
    fun findsTheIosAppThatDeclaresTheScheme() {
        val runner = FakeRunner { command ->
            when {
                command.contains("simctl list") -> success(fixture("ios/simctl-list-devices.json"))
                command.contains("simctl listapps") -> success("plist")
                command.contains("-convert json") -> success(fixture("ios/listapps.json"))
                command.contains("MobileCal.app/Info.plist") -> success(fixture("ios/url-types-mobilecal.json"))
                else -> success("[]")
            }
        }

        val result = deeplinkCli { iosOnly(runner) }.test("resolve calshow://")

        assertEquals(0, result.statusCode)
        assertTrue(result.stdout.contains("default   Calendar (com.apple.mobilecal)"))
    }

    @Test
    fun cannotTellWhichIosAppOpensAWebLink() {
        val runner = FakeRunner { command ->
            if (command.contains("simctl list")) success(fixture("ios/simctl-list-devices.json")) else success()
        }

        val result = deeplinkCli { iosOnly(runner) }.test("resolve https://example.com")

        assertEquals(0, result.statusCode)
        assertTrue(result.stdout.startsWith("? Cannot tell which app opens https://example.com"))
    }

    @Test
    fun failsWithAToolingErrorWhenNoToolIsInstalled() {
        val result = deeplinkCli { Toolchain(adb = null, simctl = null) }.test("resolve myapp://x")

        assertEquals(4, result.statusCode)
        assertTrue(result.stderr.contains("Neither adb nor xcrun was found."))
    }
}
