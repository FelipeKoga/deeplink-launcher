package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver

internal class NativeDriverFactory : DriverFactory {
    override fun createDriver(schema: SqlSchema<QueryResult.Value<Unit>>, databaseName: String): SqlDriver =
        NativeSqliteDriver(schema, databaseName)
}
