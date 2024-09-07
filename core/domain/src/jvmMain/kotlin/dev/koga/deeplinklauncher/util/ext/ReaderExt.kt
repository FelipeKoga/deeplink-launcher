package dev.koga.deeplinklauncher.util.ext

import dev.koga.deeplinklauncher.model.ProtoText
import java.io.Reader

inline fun Reader.useProtoText(
    target: String,
    block: (ProtoText) -> Unit,
) {
    val protoTextRegex = Regex(pattern = "($target)\\s*(\\{[^}]+})")

    val builder = StringBuilder()

    useLines { lines ->
        lines.forEach { line ->

            builder.append(line)

            protoTextRegex.find(builder)?.let {
                val (name, text) = it.destructured

                block(ProtoText.from(name, text))

                builder.clear()
            }
        }
    }
}
