package dev.koga.deeplinklauncher.cli.android

import dev.koga.deeplinklauncher.cli.fixture
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AdbOutputTest {

    @Test
    fun parsesASuccessfulColdStart() {
        val start = AdbOutput.amStart(fixture("android/am-start-ok.txt"))

        assertEquals("ok", start.status)
        assertEquals("COLD", start.launchState)
        assertEquals(4451L, start.totalTimeMs)
        assertEquals(Component("com.android.chrome", "org.chromium.chrome.browser.firstrun.FirstRunActivity"), start.activity)
        assertNull(start.error)
    }

    @Test
    fun parsesAStartThatNoActivityHandles() {
        val start = AdbOutput.amStart(fixture("android/am-start-unresolved.txt"))

        assertNull(start.activity)
        assertTrue(start.error!!.startsWith("Activity not started, unable to resolve Intent"))
    }

    @Test
    fun readsTheDefaultHandlerFromResolveActivity() {
        assertEquals(
            listOf(Component("com.android.chrome", "com.google.android.apps.chrome.IntentDispatcher")),
            AdbOutput.components(fixture("android/resolve-one.txt")),
        )
        assertEquals(emptyList(), AdbOutput.components(fixture("android/resolve-none.txt")))
    }

    @Test
    fun readsEveryHandlerFromQueryActivities() {
        assertEquals(
            listOf(
                "com.google.android.dialer/com.android.dialer.main.impl.MainActivity",
                "com.google.android.contacts/com.google.android.apps.contacts.activities.NonPhoneActivity",
            ),
            AdbOutput.components(fixture("android/query-many.txt")).map(Component::flattened),
        )
        assertEquals(emptyList(), AdbOutput.components(fixture("android/query-none.txt")))
    }

    @Test
    fun readsOnlineDevicesWithTheirModel() {
        assertEquals(
            listOf(AdbDeviceLine(serial = "emulator-5554", state = "device", model = "sdk_gphone64_arm64")),
            AdbOutput.devices(fixture("android/devices-l.txt")),
        )
    }

    @Test
    fun recognisesTheSystemChooser() {
        assertTrue(Component.parse("android/com.android.internal.app.ResolverActivity")!!.isChooser)
    }

    @Test
    fun quotesTheLinkForTheDeviceShell() {
        assertEquals("'https://a.com/x?a=1&b=2'", Adb.quote("https://a.com/x?a=1&b=2"))
        assertEquals("'myapp://it'\\''s'", Adb.quote("myapp://it's"))
    }
}
