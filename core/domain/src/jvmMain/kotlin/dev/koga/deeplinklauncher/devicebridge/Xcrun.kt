package dev.koga.deeplinklauncher.devicebridge

import dev.koga.deeplinklauncher.model.Os
import dev.koga.deeplinklauncher.util.ext.installed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.Reader

class Xcrun(
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
                "simctl",
                "openurl",
                id,
                link,
            ).start().also {
                it.waitFor()
            }
        }
    }

    override fun track(): Flow<List<DeviceBridge.Device>> = flow {
        while (true) {
            val devicesProcess = ProcessBuilder(
                path,
                "simctl",
                "list",
                "devices"
            ).start()

            val bootedDevices = ProcessBuilder("grep", "Booted").start()
            bootedDevices.outputStream.bufferedWriter().use { writer ->
                devicesProcess.inputStream.bufferedReader().useLines { lines ->
                    lines.forEach { line ->
                        writer.write(line)
                        writer.newLine()
                    }
                }
            }

            devicesProcess.waitFor()
            bootedDevices.waitFor()

            bootedDevices
                .inputStream
                .bufferedReader()
                .process { protoText ->
                    val device = DeviceBridge.Device(
                        id = protoText.fields["udid"].toString(),
                        name = protoText.name,
                        platform = DeviceBridge.Platform.IOS,
                        isEmulator = true,
                    )

                    tracking.addOrUpdate(device)

                    emit(tracking)
                }

            delay(timeMillis = 10000)
        }
    }.flowOn(dispatcher)

    private inline fun Reader.process(
        block: (ProtoText) -> Unit,
    ) {
        val iOSDeviceRegex = Regex(pattern = "\\s*(.+)\\s*\\(([-A-F0-9]+)\\)\\s*\\((Booted)\\)")

        val builder = StringBuilder()

        return useLines { lines ->
            lines.forEach { line ->
                builder.append(line)
                iOSDeviceRegex.find(builder)?.let {
                    val (name, udid) = it.destructured
                    block(ProtoText.fromXcrun(name, udid))
                    builder.clear()
                }
            }
        }
    }


    companion object {
        fun build(dispatcher: CoroutineDispatcher): Xcrun {
            if ("xcrun".installed()) {
                return Xcrun("xcrun", dispatcher)
            }

            return when (Os.get()) {
                Os.MAC -> {
                    Xcrun("/usr/bin/xcrun", dispatcher)
                }

                else -> {
                    throw IllegalStateException("Xcrun is only available on macOS.")
                }
            }
        }
    }
}