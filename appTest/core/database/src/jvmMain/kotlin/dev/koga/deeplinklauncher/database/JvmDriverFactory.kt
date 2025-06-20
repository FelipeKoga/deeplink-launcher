package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

class JvmDriverFactory : DriverFactory {
    override fun createDriver(databaseName: String): SqlDriver {
        val userHome = System.getProperty("user.home")
        val databasePath = File(userHome, "$databaseName.db")
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${databasePath.absolutePath}")

        if (!databasePath.exists()) {
            DeepLinkLauncherDatabase.Schema.create(driver)
        }

        return driver
    }
}
