package dev.koga.deeplinklauncher.database.converter

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

val localDateTimeAdapter = object : ColumnAdapter<LocalDateTime, Long> {
    override fun decode(databaseValue: Long): LocalDateTime =
        Instant.fromEpochMilliseconds(databaseValue)
            .toLocalDateTime(TimeZone.currentSystemDefault())

    override fun encode(value: LocalDateTime): Long =
        value.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}
