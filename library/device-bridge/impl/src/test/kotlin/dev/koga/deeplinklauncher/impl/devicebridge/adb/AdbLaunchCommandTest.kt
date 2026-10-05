package dev.koga.deeplinklauncher.impl.devicebridge.adb

import dev.koga.deeplinklauncher.devicebridge.impl.adb.Adb
import org.junit.Assert.assertEquals
import org.junit.Test

class AdbLaunchCommandTest {

    @Test
    fun `should send the whole link as one quoted shell argument`() {
        assertEquals(
            listOf("adb", "-s", "emulator-5554", "shell", "am start -a android.intent.action.VIEW -d 'myapp://x?a=1&b=2'"),
            Adb.launchCommand("adb", "emulator-5554", "myapp://x?a=1&b=2"),
        )
    }

    @Test
    fun `should escape single quotes inside the link`() {
        assertEquals(
            "am start -a android.intent.action.VIEW -d 'myapp://it'\\''s'",
            Adb.launchCommand("adb", "emulator-5554", "myapp://it's").last(),
        )
    }
}
