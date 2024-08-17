package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.Target
import io.github.aakira.napier.log

class DeviceParser {

    private val pairRegex = Regex(pattern = "(\\S+)\\s*:\\s*\"?([^\"\\s]+)")

    operator fun invoke(input: String): Target.Device {

        log { "device: $input" }

        val pairs = mutableMapOf<String, Any>()

        pairRegex.findAll(input).forEach {

            val (key, value) = it.destructured

            pairs[key] = value
        }

        val serial = pairs["serial"] as String
        val active = pairs["state"] == "DEVICE"
        val type = pairs["connection_type"] as String

        return when (type) {
            "SOCKET" -> Target.Device.Emulator(
                serial = serial,
                active = active
            )

            else -> Target.Device.Physical(
                serial = serial,
                active = active
            )
        }
    }
}
