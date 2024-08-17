package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.Target
import dev.koga.deeplinklauncher.util.ext.dropWhile
import dev.koga.deeplinklauncher.util.ext.get
import io.github.aakira.napier.log

class DeviceParser {

    private val pattern = Regex(pattern = "(.+)\\s+(.+)")

    operator fun invoke(input: String): Target.Device {

        log { "device: $input" }

        val (serial, state) = input
            .dropWhile(n = 4, Char::isDigit)
            .get(pattern)
            .destructured

        return if (serial.startsWith(prefix = "emulator")) {
            Target.Device.Emulator(
                serial = serial,
                active = state == "device",
            )
        } else {
            Target.Device.Physical(
                serial = serial,
                active = state == "device",
            )
        }
    }
}
