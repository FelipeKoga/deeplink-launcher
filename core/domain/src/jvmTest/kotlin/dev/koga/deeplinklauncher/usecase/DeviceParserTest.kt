package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.Target
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DeviceParserTest {

    private val parser = DeviceParser()

    @Test
    fun `testing one emulator`() {

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
}
