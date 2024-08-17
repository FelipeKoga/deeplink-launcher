package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.datasource.AdbDataSource
import dev.koga.deeplinklauncher.model.ProtoText
import dev.koga.deeplinklauncher.model.Target
import io.github.aakira.napier.log

class DeviceParser(
    private val adbProgram: AdbDataSource
) {

    suspend operator fun invoke(protoText: ProtoText): Target.Device {

        log { "device: $protoText" }

        val serial = protoText.fields["serial"] as String
        val active = protoText.fields["state"] == "DEVICE"
        val type = protoText.fields["connection_type"] as String

        return when (type) {
            "SOCKET" -> Target.Device.Emulator(
                serial = serial,
                active = active,
                name = adbProgram.getEmulatorName(
                    serial = serial
                ).ifEmpty {
                    serial
                }
            )

            else -> Target.Device.Physical(
                serial = serial,
                active = active,
                name = adbProgram.getDeviceName(
                    serial = serial
                ).ifEmpty {
                    serial
                }
            )
        }
    }
}
