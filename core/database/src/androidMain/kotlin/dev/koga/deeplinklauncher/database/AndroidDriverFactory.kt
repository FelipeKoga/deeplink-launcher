package dev.koga.deeplinklauncher.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

internal class AndroidDriverFactory(
    private val context: Context,
) : DriverFactory {

    override fun createDriver(databaseName: String): SqlDriver =
        AndroidSqliteDriver(DeepLinkLauncherDatabase.Schema, context, databaseName)
}
