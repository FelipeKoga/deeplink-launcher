package dev.koga.deeplinklauncher.devicebridge

import dev.koga.deeplinklauncher.model.Os
import dev.koga.deeplinklauncher.util.ext.installed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.Reader

class Adb(
    private val path: String,
    private val dispatcher: CoroutineDispatcher,
) : DeviceBridge {

    override val installed get() = path.installed()

    private val tracking = mutableListOf<DeviceBridge.Device>()

    override val devices: List<DeviceBridge.Device>
        get() = tracking.toList()

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
        ProcessBuilder(
            path,
            "track-devices",
            "--proto-text",
        )
            .start()
            .inputStream
            .bufferedReader()
            .process { protoText ->
                val serial = protoText.fields["serial"] as String
                val type = protoText.fields["connection_type"] as String

                val device = when (type) {
                    "SOCKET" -> DeviceBridge.Device(
                        id = serial,
                        name = getEmulatorName(
                            serial = serial,
                        ).ifEmpty {
                            serial
                        },
                        platform = DeviceBridge.Platform.ANDROID,
                        isEmulator = true,
                    )

                    else -> DeviceBridge.Device(
                        id = serial,
                        name = getDeviceName(
                            serial = serial,
                        ).ifEmpty {
                            serial
                        },
                        platform = DeviceBridge.Platform.ANDROID,
                        isEmulator = false,
                    )
                }

                tracking.addOrUpdate(device)

                emit(tracking)
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

    private suspend fun getDeviceModel(serial: String): String {
        return getProperty(
            serial = serial,
            key = "ro.product.model",
        )
    }

    private suspend fun getEmulatorName(serial: String): String {
        return getProperty(
            serial = serial,
            key = "ro.kernel.qemu.avd_name",
        )
    }

    private inline fun Reader.process(
        target: String = "device",
        block: (ProtoText) -> Unit,
    ) {
        val protoTextRegex = Regex(pattern = "($target)\\s*(\\{[^}]+})")

        val builder = StringBuilder()

        return useLines { lines ->
            lines.forEach { line ->
                builder.append(line)
                protoTextRegex.find(builder)?.let {
                    val (name, text) = it.destructured
                    block(ProtoText.fromAdb(name, text))
                    builder.clear()
                }
            }
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
                    dispatcher = dispatcher
                )
            }

            val userHome = System.getProperty("user.home")

            return when (Os.get()) {
                Os.LINUX -> {
                    Adb(
                        path = "$userHome/Android/Sdk/platform-tools/adb",
                        dispatcher = dispatcher
                    )
                }

                Os.WINDOWS -> {
                    Adb(
                        path = "$userHome/AppData/Local/Android/Sdk/platform-tools/adb",
                        dispatcher = dispatcher
                    )
                }

                Os.MAC -> {
                    Adb(
                        path = "$userHome/Library/Android/sdk/platform-tools/adb",
                        dispatcher = dispatcher
                    )
                }
            }
        }
    }
}
