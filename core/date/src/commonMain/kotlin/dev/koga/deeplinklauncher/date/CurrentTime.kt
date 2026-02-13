package dev.koga.deeplinklauncher.date

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

val currentLocalDateTime
    get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
