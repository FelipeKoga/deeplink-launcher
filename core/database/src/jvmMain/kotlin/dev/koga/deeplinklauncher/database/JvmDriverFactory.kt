package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.koga.deeplinklauncher.platform.JvmAppDataDirectory
import dev.koga.deeplinklauncher.platform.migrateFileIfNeeded
import java.io.File
import java.sql.DriverManager
import java.util.Properties

class JvmDriverFactory : DriverFactory {
    override fun createDriver(databaseName: String): SqlDriver {
        val appDir = JvmAppDataDirectory.resolve()
        val databasePath = File(appDir, "$databaseName.db")
        val legacyPath = File(System.getProperty("user.home"), "$databaseName.db")

        migrateFileIfNeeded(legacyFile = legacyPath, targetFile = databasePath)

        return createJvmDriver(databasePath)
    }
}

internal fun createJvmDriver(databaseFile: File): SqlDriver {
    val url = "jdbc:sqlite:${databaseFile.absolutePath}"

    markUnversionedDatabase(url)

    return JdbcSqliteDriver(
        url = url,
        properties = Properties(),
        schema = DeepLinkLauncherDatabase.Schema,
    )
}

private const val UNVERSIONED_SCHEMA_VERSION = 1

private fun markUnversionedDatabase(url: String) {
    DriverManager.getConnection(url).use { connection ->
        connection.createStatement().use { statement ->
            val version = statement.executeQuery("PRAGMA user_version").use { it.getInt(1) }
            if (version != 0) return

            val hasTables = statement.executeQuery(
                "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = 'deeplink'",
            ).use { it.next() }

            if (hasTables) {
                statement.executeUpdate("PRAGMA user_version = $UNVERSIONED_SCHEMA_VERSION")
            }
        }
    }
}
