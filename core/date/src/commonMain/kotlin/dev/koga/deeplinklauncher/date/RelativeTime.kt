package dev.koga.deeplinklauncher.date

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

fun LocalDateTime.formatRelativeToNow(now: LocalDateTime = currentLocalDateTime): String {
    val timeZone = TimeZone.currentSystemDefault()
    val elapsed = now.toInstant(timeZone) - toInstant(timeZone)

    return when {
        elapsed < 1.minutes -> "Just now"
        elapsed < 1.hours -> "${elapsed.inWholeMinutes}m ago"
        elapsed < 1.days -> "${elapsed.inWholeHours}h ago"
        elapsed < 7.days -> "${elapsed.inWholeDays}d ago"
        else -> format("MMM d, yyyy")
    }
}
