package dev.koga.deeplinklauncher.date

import kotlinx.datetime.LocalDateTime

expect fun LocalDateTime.format(format: String): String
