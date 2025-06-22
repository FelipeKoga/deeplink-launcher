package dev.koga.deeplinklauncher.devicebridge.api

import kotlinx.coroutines.flow.Flow

interface DeviceBridge {
    val installed: Boolean
    val devices: List<Device>

    fun track(): Flow<List<Device>>

    suspend fun launch(
        id: String,
        link: String,
    ): Process

    data class Device(
        val id: String,
        val name: String,
        val active: Boolean,
        val isEmulator: Boolean,
        val platform: Platform,
    )

    enum class Platform {
        ANDROID,
        IOS,
    }
}
