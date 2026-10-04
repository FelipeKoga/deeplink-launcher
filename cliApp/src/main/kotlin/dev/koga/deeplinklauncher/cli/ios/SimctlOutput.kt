package dev.koga.deeplinklauncher.cli.ios

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal data class Simulator(
    val udid: String,
    val name: String,
    val booted: Boolean,
    val osVersion: String?,
)

internal data class IosApp(
    val bundleId: String,
    val name: String,
    val path: String,
)

internal object SimctlOutput {

    private val json = Json { ignoreUnknownKeys = true }

    fun simulators(text: String): List<Simulator> =
        json.decodeFromString<DeviceList>(text).devices.flatMap { (runtime, devices) ->
            devices.map { device ->
                Simulator(
                    udid = device.udid,
                    name = device.name,
                    booted = device.state == "Booted",
                    osVersion = runtimeVersion(runtime),
                )
            }
        }

    fun apps(text: String): List<IosApp> =
        json.decodeFromString<Map<String, AppEntry>>(text).map { (bundleId, app) ->
            IosApp(
                bundleId = bundleId,
                name = app.displayName ?: app.name ?: bundleId,
                path = app.path,
            )
        }

    fun urlSchemes(text: String): List<String> =
        json.decodeFromString<List<UrlType>>(text).flatMap { it.schemes }

    fun runningBundleIds(text: String): Set<String> =
        Regex("""UIKitApplication:([^\[\s]+)""").findAll(text).map { it.groupValues[1] }.toSet()

    private fun runtimeVersion(runtime: String): String? =
        Regex("""SimRuntime\.iOS-(\d+(?:-\d+)*)""").find(runtime)?.groupValues?.get(1)?.replace('-', '.')

    @Serializable
    private data class DeviceList(val devices: Map<String, List<DeviceEntry>>)

    @Serializable
    private data class DeviceEntry(val udid: String, val name: String, val state: String)

    @Serializable
    private data class AppEntry(
        @SerialName("CFBundleDisplayName") val displayName: String? = null,
        @SerialName("CFBundleName") val name: String? = null,
        @SerialName("Path") val path: String,
    )

    @Serializable
    private data class UrlType(@SerialName("CFBundleURLSchemes") val schemes: List<String> = emptyList())
}
