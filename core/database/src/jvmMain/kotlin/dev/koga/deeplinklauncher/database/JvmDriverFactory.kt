package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File
import java.util.Properties

class JvmDriverFactory : DriverFactory {
    override fun createDriver(databaseName: String): SqlDriver {
        val userHome = System.getProperty("user.home")
        val databasePath = File(userHome, "$databaseName.db")

        return JdbcSqliteDriver(
            url = "jdbc:sqlite:${databasePath.absolutePath}",
            properties = Properties(),
            schema = DeepLinkLauncherDatabase.Schema,
        )
    }
}
