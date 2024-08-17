package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.Target
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DeviceParserTest {

    private val parser = DeviceParser()

    @Test
    fun `testing parser`() {

        val activeAndUsb = """
                device {
                    serial: "ZF523HKK7K"
                    state: DEVICE
                    connection_type: USB
                }
            """.trimIndent()

        val offlineAndSocket = """
                device {
                    serial: "emulator-5554"
                    state: OFFLINE
                    connection_type: SOCKET
                }
            """.trimIndent()

        val target = listOf(
            activeAndUsb,
            offlineAndSocket
        )

        val expected = listOf(
            Target.Device.Physical(
                serial = "ZF523HKK7K",
                active = true,
            ),
            Target.Device.Emulator(
                serial = "emulator-5554",
                active = false,
            )
        )

        assertEquals(expected, target.map(parser::invoke))
    }
}
