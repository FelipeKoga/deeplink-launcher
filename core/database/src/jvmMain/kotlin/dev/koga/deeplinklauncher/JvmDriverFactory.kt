package dev.koga.deeplinklauncher

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.DriverFactory
import dev.koga.deeplinklauncher.model.DeepLink

class JvmDriverFactory : DriverFactory {
    override fun createDriver(databaseName: String): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        DeepLinkLauncherDatabase.Schema.create(driver)
        return driver
    }

    override fun shouldPrepopulateDatabase(databaseName: String): Boolean {
        return false
    }

    override fun getPrepopulateData(): List<DeepLink> {
        return emptyList()
    }
}