package dev.koga.deeplinklauncher.util.ext

import java.io.Reader

val String.protoTextRegex
    get() = Regex(pattern = "($this)\\s\\{[^}]+}")

inline fun Reader.useProtoText(
    name: String,
    block: (String) -> Unit
) {

    val protoTextRegex = name.protoTextRegex

    val builder = StringBuilder()

    useLines { lines ->
        lines.forEach { line ->

            builder.append(line)

            protoTextRegex.find(builder)?.let {
                block(it.value)
                builder.clear()
            }
        }
    }
}
