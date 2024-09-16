package dev.koga.deeplinklauncher.devicebridge.xcrun

import dev.koga.deeplinklauncher.devicebridge.DeviceBridge
import dev.koga.deeplinklauncher.model.Os
import dev.koga.deeplinklauncher.util.ext.installed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.withContext

internal class Xcrun private constructor(
    private val path: String,
    private val dispatcher: CoroutineDispatcher,
) : DeviceBridge {

    private val tracking = MutableStateFlow<List<DeviceBridge.Device>>(emptyList())
    override val devices: List<DeviceBridge.Device>
        get() = tracking.value

    override val installed get() = path.installed()

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
            val inputStream = ProcessBuilder(
                path,
                "simctl",
                "list",
                "--json",
                "devices",
                "available"
            ).start().also {
                it.waitFor()
            }.inputStream

            val devices = XcrunParser.parse(inputStream).map {
                DeviceBridge.Device(
                    id = it.udid,
                    name = it.name,
                    platform = DeviceBridge.Platform.IOS,
                    isEmulator = true,
                    active = it.state == "Booted",
                )
            }

            emit(tracking.updateAndGet { devices })

            delay(timeMillis = XCRUN_TRACK_DEVICES_DELAY)
        }
    }.flowOn(dispatcher)

    companion object {
        private const val XCRUN_TRACK_DEVICES_DELAY = 5000L
        val XCRUN_DEVICES_REGEX = Regex(
            pattern = "\\s*(.+?)\\s*\\(([-A-F0-9]+)\\)\\s*\\((Booted|Shutdown)\\)",
            RegexOption.IGNORE_CASE
        )

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