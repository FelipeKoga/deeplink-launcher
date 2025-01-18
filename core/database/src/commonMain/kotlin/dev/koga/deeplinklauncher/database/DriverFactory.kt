package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.SqlDriver

internal interface DriverFactory {
    fun createDriver(databaseName: String): SqlDriver
}
