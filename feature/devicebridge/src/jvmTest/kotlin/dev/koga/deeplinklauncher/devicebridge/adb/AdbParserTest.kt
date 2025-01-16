package dev.koga.deeplinklauncher.devicebridge.adb

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AdbParserTest {

    @Test
    fun `should parse adb devices correctly`() = runTest {
        val input = """
                device {
                    serial: "ZF523HKK7K"
                    state: DEVICE
                    connection_type: USB
                }
                device {
                    serial: "emulator-5554"
                    state: DEVICE
                    connection_type: SOCKET
                }
        """.trimIndent()

        val inputStream = input.byteInputStream()

        val devices = mutableListOf<AdbDevice>()

        AdbParser.parse(inputStream) { adbDevice ->
            devices.add(adbDevice)
        }

        assertEquals(
            AdbDevice(
                serial = "ZF523HKK7K",
                state = "DEVICE",
                connectionType = "USB",
            ),
            devices[0],
        )

        assertEquals(
            AdbDevice(
                serial = "emulator-5554",
                state = "DEVICE",
                connectionType = "SOCKET",
            ),
            devices[1],
        )
    }
}
