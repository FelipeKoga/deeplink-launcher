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
        val isEmulator: Boolean,
        val platform: Platform
    )

    enum class Platform {
        ANDROID,
        IOS
    }
}

fun MutableList<DeviceBridge.Device>.addOrUpdate(device: DeviceBridge.Device) {
    val index = indexOfFirst { it.id == device.id }

    if (index == -1) {
        add(device)
    } else {
        set(index, device)
    }
}
