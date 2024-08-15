package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.Target

class DeviceParser {

    operator fun invoke(input: String): Target.Device {

        val parts = input.dropWhile {
            it.isDigit()
        }.split(Regex("\\s+"))

        return Target.Device(
            name = parts[0],
            type = parts[1]
        )
    }
}
