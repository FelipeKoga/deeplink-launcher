package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.Target

class DeviceParser {

    operator fun invoke(input: String): Target.Device {

        val (serial, state) = input.dropWhile {
            it.isDigit()
        }.split(Regex("\\s+"))

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
