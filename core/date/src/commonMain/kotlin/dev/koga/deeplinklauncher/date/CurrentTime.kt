package dev.koga.deeplinklauncher.date

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val currentLocalDateTime
    get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())