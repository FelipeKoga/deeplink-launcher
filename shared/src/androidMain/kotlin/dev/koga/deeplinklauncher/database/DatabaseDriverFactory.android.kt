package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.SqlDriver
import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory(private val context: Context) {

    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(
            schema = DeepLinkLauncherDatabase.Schema,
            context = context,
            name = "DLL.Database.db"
        )
}