package dev.koga.deeplinklauncher.devicebridge.impl

import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge
import dev.koga.deeplinklauncher.devicebridge.api.ForegroundActivity
import dev.koga.deeplinklauncher.devicebridge.api.ProcessResult
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

    override suspend fun getForegroundActivity(id: String): ForegroundActivity? {
        val device = devices.firstOrNull { it.id == id } ?: return null
        return when (device.platform) {
            DeviceBridge.Platform.ANDROID -> adb.getForegroundActivity(id)
            DeviceBridge.Platform.IOS -> null
        }
    }

    override suspend fun dumpUiHierarchy(id: String): String {
        val device = devices.firstOrNull { it.id == id } ?: return ""
        return when (device.platform) {
            DeviceBridge.Platform.ANDROID -> adb.dumpUiHierarchy(id)
            DeviceBridge.Platform.IOS -> ""
        }
    }

    override suspend fun shell(id: String, command: List<String>): ProcessResult {
        val device = devices.firstOrNull { it.id == id }
            ?: return ProcessResult(exitCode = 1, stdout = "", stderr = "Device not found")
        return when (device.platform) {
            DeviceBridge.Platform.ANDROID -> adb.shell(id, command)
            DeviceBridge.Platform.IOS -> ProcessResult(
                exitCode = 1,
                stdout = "",
                stderr = "Shell is not supported for iOS devices",
            )
        }
    }
}
