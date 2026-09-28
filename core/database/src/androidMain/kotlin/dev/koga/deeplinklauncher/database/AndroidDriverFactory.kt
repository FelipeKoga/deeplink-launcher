package dev.koga.deeplinklauncher.database

import android.content.Context
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

internal class AndroidDriverFactory(
    private val context: Context,
) : DriverFactory {

    override fun createDriver(schema: SqlSchema<QueryResult.Value<Unit>>, databaseName: String): SqlDriver =
        AndroidSqliteDriver(schema, context, databaseName)
}
