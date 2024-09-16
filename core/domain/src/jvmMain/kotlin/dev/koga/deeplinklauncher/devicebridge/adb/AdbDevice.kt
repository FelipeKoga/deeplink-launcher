package dev.koga.deeplinklauncher.devicebridge.adb

internal data class AdbDevice(
    val serial: String,
    val connectionType: String,
    val state: String,
)
