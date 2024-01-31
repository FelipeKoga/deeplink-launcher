package dev.koga.deeplinklauncher.util.ext

import kotlinx.datetime.LocalDateTime

expect fun LocalDateTime.format(format: String): String
