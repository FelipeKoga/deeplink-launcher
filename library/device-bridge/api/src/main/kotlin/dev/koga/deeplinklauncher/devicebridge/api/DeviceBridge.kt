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

    suspend fun getForegroundActivity(id: String): ForegroundActivity?

    suspend fun dumpUiHierarchy(id: String): String

    suspend fun shell(id: String, command: List<String>): ProcessResult

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

data class ForegroundActivity(
    val packageName: String,
    val activityClass: String,
)

data class ProcessResult(
    val exitCode: Int,
    val stdout: String,
    val stderr: String,
) {
    val isSuccess: Boolean
        get() = exitCode == 0
}
