package dev.koga.deeplinklauncher.cli.device

import dev.koga.deeplinklauncher.cli.CliFailure
import dev.koga.deeplinklauncher.cli.ExitCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DeviceSelectorTest {

    private val pixel = Device("emulator-5554", "Pixel", Platform.ANDROID, virtual = true)
    private val iphone = Device("564F", "iPhone 16", Platform.IOS, virtual = true)

    @Test
    fun picksTheOnlyRunningDevice() {
        assertEquals(pixel, DeviceSelector.select(listOf(pixel), id = null, platform = null))
    }

    @Test
    fun narrowsByPlatform() {
        assertEquals(iphone, DeviceSelector.select(listOf(pixel, iphone), id = null, platform = Platform.IOS))
    }

    @Test
    fun picksTheRequestedDevice() {
        assertEquals(iphone, DeviceSelector.select(listOf(pixel, iphone), id = "564F", platform = null))
    }

    @Test
    fun refusesToGuessBetweenSeveralDevices() {
        val failure = assertFailsWith<CliFailure> { DeviceSelector.select(listOf(pixel, iphone), id = null, platform = null) }

        assertEquals(ExitCode.DEVICE, failure.exitCode)
        assertTrue(failure.hint!!.contains("--device emulator-5554"))
        assertTrue(failure.hint!!.contains("--device 564F"))
    }

    @Test
    fun failsWithADeviceErrorWhenNothingIsRunning() {
        val failure = assertFailsWith<CliFailure> { DeviceSelector.select(emptyList(), id = null, platform = Platform.ANDROID) }

        assertEquals(ExitCode.DEVICE, failure.exitCode)
    }
}
