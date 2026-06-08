package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.koga.deeplinklauncher.platform.JvmAppDataDirectory
import dev.koga.deeplinklauncher.platform.migrateFileIfNeeded
import java.io.File
import java.util.Properties

class JvmDriverFactory : DriverFactory {
    override fun createDriver(databaseName: String): SqlDriver {
        val appDir = JvmAppDataDirectory.resolve()
        val databasePath = File(appDir, "$databaseName.db")
        val legacyPath = File(System.getProperty("user.home"), "$databaseName.db")

        migrateFileIfNeeded(legacyFile = legacyPath, targetFile = databasePath)

        return JdbcSqliteDriver(
            url = "jdbc:sqlite:${databasePath.absolutePath}",
            properties = Properties(),
            schema = DeepLinkLauncherDatabase.Schema,
        )
    }
}
