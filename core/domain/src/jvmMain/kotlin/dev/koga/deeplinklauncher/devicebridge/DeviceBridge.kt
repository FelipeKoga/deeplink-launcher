package dev.koga.deeplinklauncher.devicebridge

import kotlinx.coroutines.flow.Flow

interface DeviceBridge {

    val installed: Boolean
    val devices: List<Device>

    fun track(): Flow<List<Device>>
    suspend fun launch(id: String, link: String): Process

    data class Device(
        val id: String,
        val name: String,
        val active: Boolean,
        val isEmulator: Boolean,
        val platform: Platform
    )

    enum class Platform {
        ANDROID,
        IOS
    }
}

fun List<DeviceBridge.Device>.addOrReplace(device: DeviceBridge.Device): List<DeviceBridge.Device> {
    val foundDevice = firstOrNull { it.id == device.id }

    return if (foundDevice == null) this + device
    else this.map { if (it == foundDevice) device else it }
}
