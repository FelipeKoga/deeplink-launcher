package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

internal class NativeDriverFactory : DriverFactory {
    override fun createDriver(databaseName: String): SqlDriver =
        NativeSqliteDriver(DeepLinkLauncherDatabase.Schema, databaseName)
}
