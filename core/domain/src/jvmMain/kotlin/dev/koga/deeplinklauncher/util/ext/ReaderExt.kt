package dev.koga.deeplinklauncher.util.ext

import dev.koga.deeplinklauncher.model.ProtoText
import io.github.aakira.napier.log
import java.io.Reader

inline fun Reader.useProtoText(
    target: String,
    block: (ProtoText) -> Unit,
) {
    val protoTextRegex = Regex(pattern = "($target)\\s*(\\{[^}]+})")
    val iOSDeviceRegex = Regex(pattern = "\\s*(.+)\\s*\\(([-A-F0-9]+)\\)\\s*\\((Booted)\\)")

    val builder = StringBuilder()

    useLines { lines ->
        lines.forEach { line ->
            builder.append(line)

            iOSDeviceRegex.find(builder)?.let {
                val (name, udid) = it.destructured
                println(udid)
                block(ProtoText.fromXcrun(name, udid))
                builder.clear()
            }

            protoTextRegex.find(builder)?.let {
                val (name, text) = it.destructured
                block(ProtoText.fromAdb(name, text))
                builder.clear()
            }
        }
    }
}
