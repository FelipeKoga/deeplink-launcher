package dev.koga.deeplinklauncher.date

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

actual fun LocalDateTime.format(format: String): String {
    return DateTimeFormatter.ofPattern(format).format(this.toJavaLocalDateTime())
}
