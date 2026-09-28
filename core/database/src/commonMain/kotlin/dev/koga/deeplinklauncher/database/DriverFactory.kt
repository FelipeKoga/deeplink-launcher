package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

interface DriverFactory {
    fun createDriver(schema: SqlSchema<QueryResult.Value<Unit>>, databaseName: String): SqlDriver
}
