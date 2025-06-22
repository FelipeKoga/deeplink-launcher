package dev.koga.deeplinklauncher.devicebridge.impl

import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge
import dev.koga.deeplinklauncher.devicebridge.impl.adb.Adb
import dev.koga.deeplinklauncher.devicebridge.impl.xcrun.Xcrun
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

internal class CompositeDeviceBridge internal constructor(
    private val adb: Adb,
    private val xcrun: Xcrun,
) : DeviceBridge {

    override val installed: Boolean
        get() = adb.installed || xcrun.installed

    override val devices: List<DeviceBridge.Device>
        get() = adb.devices + xcrun.devices

    override fun track(): Flow<List<DeviceBridge.Device>> {
        return combine(
            adb.track(),
            xcrun.track(),
        ) { adb, xcrun -> adb + xcrun }
    }

    override suspend fun launch(id: String, link: String): Process {
        val device = devices.first { it.id == id }

        return when (device.platform) {
            DeviceBridge.Platform.ANDROID -> adb.launch(id, link)
            DeviceBridge.Platform.IOS -> xcrun.launch(id, link)
        }
    }
}
