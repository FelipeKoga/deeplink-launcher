package dev.koga.deeplinklauncher.manager

import dev.koga.deeplinklauncher.model.Os
import dev.koga.deeplinklauncher.util.ext.installed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface XcrunManager {

    val installed: Boolean

    suspend fun execute(
        udid: String,
        arg: String,
    ): Process

    suspend fun trackDevices(): Process

    suspend fun getProperty(udid: String, key: String): String

    suspend fun getDeviceName(udid: String): String

    suspend fun getEmulatorName(udid: String): String

    suspend fun getDeviceModel(udid: String): String
}

internal class XcrunManagerImpl(
    private val path: String,
) : XcrunManager {

    override val installed get() = path.installed()

    override suspend fun execute(
        udid: String,
        arg: String,
    ): Process {
        return withContext(Dispatchers.IO) {
            ProcessBuilder(
                path,
                "simctl",
                "openurl",
                udid,
                arg,
            ).start().also {
                it.waitFor()
            }
        }
    }

    override suspend fun trackDevices(): Process {
        return withContext(Dispatchers.IO) {
            val devices = ProcessBuilder(
                path,
                "simctl",
                "list",
                "devices"
            ).start().also {
                it.waitFor()
            }

            val grepBootedOnly = ProcessBuilder("grep", "Booted").start()

            // Redireciona a saída do primeiro processo para o segundo processo (grep)
            grepBootedOnly.outputStream.bufferedWriter().use { writer ->
                devices.inputStream.bufferedReader().useLines { lines ->
                    lines.forEach { line ->
                        writer.write(line)
                        writer.newLine()
                    }
                }
            }

            // Lê a saída final após o grep
            val result = grepBootedOnly

            // Espera ambos os processos terminarem
            devices.waitFor()
            grepBootedOnly.waitFor()

            result
        }
    }

    override suspend fun getProperty(udid: String, key: String): String {
        val process = withContext(Dispatchers.IO) {
            ProcessBuilder(
                path,
                "simctl", "getenv",
                udid,
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

    override suspend fun getDeviceName(udid: String): String {
        val process = withContext(Dispatchers.IO) {
            ProcessBuilder(
                path,
                "simctl", "getenv",
                udid,
                "SIMULATOR_DEVICE_NAME",
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

    override suspend fun getDeviceModel(udid: String): String {
        return getProperty(
            udid = udid,
            key = "SIMULATOR_MODEL_IDENTIFIER",
        )
    }

    override suspend fun getEmulatorName(udid: String): String {
        return getDeviceName(udid)
    }

    companion object {
        fun build(): XcrunManager {

            if ("xcrun".installed()) {
                return XcrunManagerImpl("xcrun")
            }

            return when (Os.get()) {
                Os.MAC -> {
                    XcrunManagerImpl("/usr/bin/xcrun")
                }

                else -> {
                    throw IllegalStateException("Xcrun is only available on macOS.")
                }
            }
        }
    }
}