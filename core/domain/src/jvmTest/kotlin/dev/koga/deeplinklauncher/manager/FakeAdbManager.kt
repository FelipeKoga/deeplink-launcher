package dev.koga.deeplinklauncher.manager

import dev.koga.deeplinklauncher.model.FakeDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FakeAdbManager : AdbManager {

    override var installed: Boolean = false

    var devices = mutableMapOf<String, FakeDevice>()

    override suspend fun execute(
        serial: String,
        action: String,
        arg: String,
    ): Process {
        return withContext(Dispatchers.IO) {
            ProcessBuilder("echo", "starting activity").start()
        }
    }

    override suspend fun trackDevices(): Process {
        return withContext(Dispatchers.IO) {
            ProcessBuilder("echo", "tracking devices").start()
        }
    }

    override suspend fun getProperty(
        serial: String,
        key: String,
    ): String {
        return checkNotNull(devices[serial]).properties[key] ?: ""
    }

    override suspend fun getDeviceName(serial: String): String {
        return checkNotNull(devices[serial]).name
    }

    override suspend fun getEmulatorName(serial: String): String {
        return getProperty(serial, "emulator_name")
    }

    override suspend fun getDeviceModel(serial: String): String {
        return getProperty(serial, "device_model")
    }
}
