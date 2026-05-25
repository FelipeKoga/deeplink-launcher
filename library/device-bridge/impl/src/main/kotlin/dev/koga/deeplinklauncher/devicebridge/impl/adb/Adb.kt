package dev.koga.deeplinklauncher.devicebridge.impl.adb

import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge
import dev.koga.deeplinklauncher.devicebridge.api.ForegroundActivity
import dev.koga.deeplinklauncher.devicebridge.api.ProcessResult
import dev.koga.deeplinklauncher.devicebridge.api.model.Os
import dev.koga.deeplinklauncher.devicebridge.impl.ext.installed
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
            ).start().apply {
                waitFor()
            }
        }
    }

    override suspend fun getForegroundActivity(id: String): ForegroundActivity? {
        val topResult = shell(id, listOf("cmd", "activity", "top"))
        if (topResult.isSuccess && topResult.stdout.isNotBlank()) {
            parseForegroundFromActivityTop(topResult.stdout)?.let { return it }
        }

        val dumpsysResult = shell(id, listOf("dumpsys", "activity", "activities"))
        if (dumpsysResult.isSuccess && dumpsysResult.stdout.isNotBlank()) {
            parseForegroundFromDumpsys(dumpsysResult.stdout)?.let { return it }
        }

        return null
    }

    override suspend fun dumpUiHierarchy(id: String): String {
        val dumpResult = shell(
            id = id,
            command = listOf("uiautomator", "dump", "/dev/tty"),
        )

        if (dumpResult.isSuccess && dumpResult.stdout.isNotBlank()) {
            return dumpResult.stdout
        }

        val fallbackResult = shell(
            id = id,
            command = listOf("sh", "-c", "uiautomator dump /sdcard/window_dump.xml && cat /sdcard/window_dump.xml"),
        )

        return if (fallbackResult.isSuccess) fallbackResult.stdout else ""
    }

    override suspend fun shell(id: String, command: List<String>): ProcessResult {
        return withContext(dispatcher) {
            val process = ProcessBuilder(
                buildList {
                    add(path)
                    add("-s")
                    add(id)
                    add("shell")
                    addAll(command)
                },
            ).start()

            val stdout = process.inputStream.bufferedReader().readText()
            val stderr = process.errorStream.bufferedReader().readText()
            val exitCode = process.waitFor()

            ProcessResult(
                exitCode = exitCode,
                stdout = stdout.trim(),
                stderr = stderr.trim(),
            )
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
            val device = if (adbDevice.connectionType == "SOCKET") {
                DeviceBridge.Device(
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
            } else {
                DeviceBridge.Device(
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
            ).start().apply {
                waitFor()
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
            ).start().apply {
                waitFor()
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
        private const val CACHE_SIZE = 128

        private val activityTopPattern = Regex(
            """ACTIVITY\s+([^\s/]+)/([^\s]+)\s+\d+\s+pid=\d+""",
        )
        private val resumedActivityPattern = Regex(
            """mResumedActivity:\s+ActivityRecord\{[^ ]+ [^ ]+ ([^\s/]+)/([^\s}]+)""",
        )
        private val topResumedActivityPattern = Regex(
            """topResumedActivity=ActivityRecord\{[^ ]+ [^ ]+ ([^\s/]+)/([^\s}]+)""",
        )

        private fun parseForegroundFromActivityTop(output: String): ForegroundActivity? {
            return activityTopPattern.find(output)?.let { match ->
                ForegroundActivity(
                    packageName = match.groupValues[1],
                    activityClass = match.groupValues[2],
                )
            }
        }

        private fun parseForegroundFromDumpsys(output: String): ForegroundActivity? {
            topResumedActivityPattern.find(output)?.let { match ->
                return ForegroundActivity(
                    packageName = match.groupValues[1],
                    activityClass = match.groupValues[2],
                )
            }

            return resumedActivityPattern.find(output)?.let { match ->
                ForegroundActivity(
                    packageName = match.groupValues[1],
                    activityClass = match.groupValues[2],
                )
            }
        }

        fun build(dispatcher: CoroutineDispatcher): Adb {
            val userHome = System.getProperty("user.home")

            return when {
                "adb".installed() -> Adb(
                    path = "adb",
                    dispatcher = dispatcher,
                )

                System.getenv("ANDROID_HOME") != null -> Adb(
                    path = "${System.getenv("ANDROID_HOME")}/platform-tools/adb",
                    dispatcher = dispatcher,
                )

                Os.get() == Os.WINDOWS -> Adb(
                    path = "$userHome/AppData/Local/Android/Sdk/platform-tools/adb",
                    dispatcher = dispatcher,
                )

                Os.get() == Os.MAC -> Adb(
                    path = "$userHome/Library/Android/sdk/platform-tools/adb",
                    dispatcher = dispatcher,
                )

                else -> Adb(
                    path = "$userHome/Android/Sdk/platform-tools/adb",
                    dispatcher = dispatcher,
                )
            }
        }
    }
}
