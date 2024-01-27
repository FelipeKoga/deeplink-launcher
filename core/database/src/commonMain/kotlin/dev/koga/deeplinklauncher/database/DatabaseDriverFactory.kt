package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.SqlDriver
import dev.koga.deeplinklauncher.model.DeepLink as DomainDeepLink

internal interface DriverFactory {
    fun createDriver(databaseName: String): SqlDriver
    fun shouldPrepopulateDatabase(databaseName: String): Boolean
    fun getPrepopulateData(): List<DomainDeepLink>
}