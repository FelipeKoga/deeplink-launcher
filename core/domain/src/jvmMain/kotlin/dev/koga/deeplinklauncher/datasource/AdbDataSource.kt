package dev.koga.deeplinklauncher.datasource

interface AdbDataSource {

    val installed: Boolean

    suspend fun startActivity(
        serial: String,
        action: String,
        arg: String,
    ): Process

    suspend fun trackDevices(): Process

    suspend fun getProperty(serial: String, key: String): String

    suspend fun getDeviceName(serial: String): String

    suspend fun getEmulatorName(serial: String): String

    suspend fun getDeviceModel(serial: String): String
}
