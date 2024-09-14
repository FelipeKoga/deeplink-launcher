package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.manager.FakeAdbManager
import dev.koga.deeplinklauncher.model.FakeDevice
import dev.koga.deeplinklauncher.devicebridge.ProtoText
import dev.koga.deeplinklauncher.model.Target
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetDeviceTypeTest {

    private lateinit var getDeviceType: GetDeviceType
    private lateinit var adbManager: FakeAdbManager

    @Before
    fun setUp() {
        adbManager = FakeAdbManager()
        getDeviceType = GetDeviceType(adbManager)
    }

    @Test
    fun `testing parser`() = runTest {
        adbManager.devices["ZF523HKK7K"] = FakeDevice(
            name = "Pixel 3",
        )

        adbManager.devices["emulator-5554"] = FakeDevice(
            properties = mapOf(
                "emulator_name" to "Nexus 5X",
            ),
        )

        val target = listOf(
            ProtoText(
                name = "device",
                fields = mapOf(
                    "serial" to "ZF523HKK7K",
                    "state" to "DEVICE",
                    "connection_type" to "USB",
                ),
            ),
            ProtoText(
                name = "device",
                fields = mapOf(
                    "serial" to "emulator-5554",
                    "state" to "OFFLINE",
                    "connection_type" to "SOCKET",
                ),
            ),
        )

        val expected = listOf(
            Target.Device.Physical(
                id = "ZF523HKK7K",
                name = "Pixel 3",
                active = true,
            ),
            Target.Device.Emulator(
                id = "emulator-5554",
                name = "Nexus 5X",
                active = false,
            ),
        )

        assertEquals(expected, target.map { getDeviceType(it) })
    }
}
