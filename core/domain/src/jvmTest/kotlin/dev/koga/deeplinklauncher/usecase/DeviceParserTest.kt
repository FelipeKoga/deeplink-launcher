package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.Target
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DeviceParserTest {

    private val parser = DeviceParser()

    @Test
    fun `testing state`() {

        val target = listOf(
            "emulator-5554 device",
            "emulator-5554 offline",
            "0001ZF523HKK7K device",
            "0001ZF523HKK7K offline",
        )

        val expected = listOf(
            Target.Device.Emulator(
                serial = "emulator-5554",
                active = true,
            ),
            Target.Device.Emulator(
                serial = "emulator-5554",
                active = false
            ),
            Target.Device.Physical(
                serial = "ZF523HKK7K",
                active = true
            ),
            Target.Device.Physical(
                serial = "ZF523HKK7K",
                active = false
            ),
        )

        assertEquals(expected, target.map(parser::invoke))
    }

    @Test
    fun `testing serial`() {

        val target = listOf(
            "0013emulator-5554 device",
            "002aemulator-5554 device",
            "0001ZF523HKK7K device",
            "003cZF523HKK7K device",
        )

        val expected = listOf(
            Target.Device.Emulator(serial = "emulator-5554"),
            Target.Device.Emulator(serial = "emulator-5554"),
            Target.Device.Physical(serial = "ZF523HKK7K"),
            Target.Device.Physical(serial = "ZF523HKK7K"),
        )

        assertEquals(expected, target.map(parser::invoke))
    }
}
