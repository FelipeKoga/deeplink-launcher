package dev.koga.deeplinklauncher.cli.ios

import dev.koga.deeplinklauncher.cli.fixture
import kotlin.test.Test
import kotlin.test.assertEquals

class DeviceCtlOutputTest {

    @Test
    fun keepsOnlyPairedAndConnectedIosDevices() {
        assertEquals(
            listOf(PhysicalDevice("00008130-000A1234ABCD001C", "Koga's iPhone", "18.1", "enabled")),
            DeviceCtlOutput.devices(fixture("ios/devicectl-list-devices.json")),
        )
    }

    @Test
    fun readsTheLaunchedProcess() {
        assertEquals(DeviceCtlLaunch.Started(1234), DeviceCtlOutput.launch(fixture("ios/devicectl-launch-ok.json")))
    }

    @Test
    fun readsTheReasonOfAFailedLaunch() {
        assertEquals(
            DeviceCtlLaunch.Failed("The requested application com.acme.missing is not installed."),
            DeviceCtlOutput.launch(fixture("ios/devicectl-launch-failed.json")),
        )
    }

    @Test
    fun readsRunningProcessIds() {
        assertEquals(setOf(1, 961, 1234), DeviceCtlOutput.runningProcessIds(fixture("ios/devicectl-processes.json")))
    }
}
