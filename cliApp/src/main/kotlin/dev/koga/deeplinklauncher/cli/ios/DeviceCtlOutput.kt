package dev.koga.deeplinklauncher.cli.ios

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal data class PhysicalDevice(
    val udid: String,
    val name: String,
    val osVersion: String?,
    val developerMode: String?,
)

internal sealed interface DeviceCtlLaunch {
    data class Started(val processId: Int) : DeviceCtlLaunch

    data class Failed(val message: String) : DeviceCtlLaunch
}

internal object DeviceCtlOutput {

    private val json = Json { ignoreUnknownKeys = true }

    fun devices(text: String): List<PhysicalDevice> =
        json.decodeFromString<Envelope<DeviceList>>(text).result?.devices.orEmpty()
            .filter { it.hardwareProperties?.platform == "iOS" }
            .filter { it.connectionProperties?.pairingState == "paired" && it.connectionProperties.transportType != null }
            .mapNotNull { device ->
                val udid = device.hardwareProperties?.udid ?: device.identifier ?: return@mapNotNull null
                PhysicalDevice(
                    udid = udid,
                    name = device.deviceProperties?.name ?: udid,
                    osVersion = device.deviceProperties?.osVersionNumber,
                    developerMode = device.deviceProperties?.developerModeStatus,
                )
            }

    fun launch(text: String): DeviceCtlLaunch {
        val envelope = json.decodeFromString<Envelope<LaunchResult>>(text)
        val processId = envelope.result?.process?.processIdentifier
        return if (envelope.info?.outcome == "success" && processId != null) {
            DeviceCtlLaunch.Started(processId)
        } else {
            DeviceCtlLaunch.Failed(envelope.errorMessage() ?: "devicectl could not launch the app.")
        }
    }

    fun runningProcessIds(text: String): Set<Int> =
        json.decodeFromString<Envelope<ProcessList>>(text).result?.runningProcesses.orEmpty()
            .mapNotNull { it.processIdentifier }
            .toSet()

    private fun Envelope<*>.errorMessage(): String? =
        error?.get("userInfo")?.jsonObject?.get("NSLocalizedDescription")?.jsonObject
            ?.get("string")?.jsonPrimitive?.contentOrNull

    @Serializable
    private data class Envelope<T>(val info: Info? = null, val result: T? = null, val error: JsonObject? = null)

    @Serializable
    private data class Info(val outcome: String? = null)

    @Serializable
    private data class DeviceList(val devices: List<DeviceEntry> = emptyList())

    @Serializable
    private data class DeviceEntry(
        val identifier: String? = null,
        val connectionProperties: ConnectionProperties? = null,
        val deviceProperties: DeviceProperties? = null,
        val hardwareProperties: HardwareProperties? = null,
    )

    @Serializable
    private data class ConnectionProperties(val pairingState: String? = null, val transportType: String? = null)

    @Serializable
    private data class DeviceProperties(
        val name: String? = null,
        val osVersionNumber: String? = null,
        val developerModeStatus: String? = null,
    )

    @Serializable
    private data class HardwareProperties(val platform: String? = null, val udid: String? = null)

    @Serializable
    private data class LaunchResult(val process: ProcessEntry? = null)

    @Serializable
    private data class ProcessList(val runningProcesses: List<ProcessEntry> = emptyList())

    @Serializable
    private data class ProcessEntry(val processIdentifier: Int? = null)
}
