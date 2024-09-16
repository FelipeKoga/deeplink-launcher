package dev.koga.deeplinklauncher.devicebridge.adb

import dev.koga.deeplinklauncher.devicebridge.DeviceBridge
import dev.koga.deeplinklauncher.model.Os
import dev.koga.deeplinklauncher.util.ext.installed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.withContext

internal class Adb private constructor(
    private val path: String,
    private val dispatcher: CoroutineDispatcher,
) : DeviceBridge {

    override val installed get() = path.installed()

    private val tracking = MutableStateFlow<List<DeviceBridge.Device>>(emptyList())
    override val devices: List<DeviceBridge.Device>
        get() = tracking.value

    override suspend fun launch(
        id: String,
        link: String,
    ): Process {
        return withContext(dispatcher) {
            ProcessBuilder(
                path,
                "-s", id,
                "shell",
                "am", "start",
                "-a", "android.intent.action.VIEW",
                "-d", link,
            ).start().also {
                it.waitFor()
            }
        }
    }

    override fun track(): Flow<List<DeviceBridge.Device>> = flow {
        emit(emptyList())

        if (!installed) {
            return@flow
        }

        val process = ProcessBuilder(
            path,
            "track-devices",
            "--proto-text",
        ).start()

        AdbParser.parse(process.inputStream) { adbDevice ->
            val device = when (adbDevice.connectionType) {
                "SOCKET" -> DeviceBridge.Device(
                    id = adbDevice.serial,
                    name = getEmulatorName(
                        serial = adbDevice.serial,
                    ).ifEmpty {
                        adbDevice.serial
                    },
                    platform = DeviceBridge.Platform.ANDROID,
                    isEmulator = true,
                    active = adbDevice.state == "DEVICE",
                )

                else -> DeviceBridge.Device(
                    id = adbDevice.serial,
                    name = getDeviceName(
                        serial = adbDevice.serial,
                    ).ifEmpty {
                        adbDevice.serial
                    },
                    platform = DeviceBridge.Platform.ANDROID,
                    isEmulator = false,
                    active = adbDevice.state == "DEVICE",
                )
            }

            emit(
                tracking.updateAndGet { devices ->
                    devices.addOrReplace(device)
                },
            )
        }
    }.flowOn(dispatcher)

    private suspend fun getProperty(serial: String, key: String): String {
        val process = withContext(Dispatchers.IO) {
            ProcessBuilder(
                path,
                "-s",
                serial,
                "shell",
                "getprop",
                key,
            ).start().also {
                it.waitFor()
            }
        }

        return process
            .inputStream
            .bufferedReader()
            .readText()
            .trim()
    }

    private suspend fun getDeviceName(serial: String): String {
        val process = withContext(Dispatchers.IO) {
            ProcessBuilder(
                path,
                "-s",
                serial,
                "shell",
                "settings",
                "get",
                "global",
                "device_name",
            ).start().also {
                it.waitFor()
            }
        }

        return process
            .inputStream
            .bufferedReader()
            .readText()
            .trim()
    }

    private suspend fun getEmulatorName(serial: String): String {
        return getProperty(
            serial = serial,
            key = "ro.kernel.qemu.avd_name",
        )
    }

    private fun List<DeviceBridge.Device>.addOrReplace(
        device: DeviceBridge.Device,
    ): List<DeviceBridge.Device> {
        val foundDevice = firstOrNull { it.id == device.id }

        return if (foundDevice == null) {
            this + device
        } else {
            this.map { if (it == foundDevice) device else it }
        }
    }

    companion object {
        fun build(dispatcher: CoroutineDispatcher): Adb {
            if ("adb".installed()) {
                return Adb(path = "adb", dispatcher = dispatcher)
            }

            System.getenv("ANDROID_HOME")?.let {
                return Adb(
                    path = it,
                    dispatcher = dispatcher,
                )
            }

            val userHome = System.getProperty("user.home")

            return when (Os.get()) {
                Os.LINUX -> {
                    Adb(
                        path = "$userHome/Android/Sdk/platform-tools/adb",
                        dispatcher = dispatcher,
                    )
                }

                Os.WINDOWS -> {
                    Adb(
                        path = "$userHome/AppData/Local/Android/Sdk/platform-tools/adb",
                        dispatcher = dispatcher,
                    )
                }

                Os.MAC -> {
                    Adb(
                        path = "$userHome/Library/Android/sdk/platform-tools/adb",
                        dispatcher = dispatcher,
                    )
                }
            }
        }
    }
}
