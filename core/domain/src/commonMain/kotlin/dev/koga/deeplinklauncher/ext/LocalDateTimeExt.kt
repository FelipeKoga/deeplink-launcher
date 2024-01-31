package dev.koga.deeplinklauncher.ext

import kotlinx.datetime.LocalDateTime

expect fun LocalDateTime.format(format: String): String
