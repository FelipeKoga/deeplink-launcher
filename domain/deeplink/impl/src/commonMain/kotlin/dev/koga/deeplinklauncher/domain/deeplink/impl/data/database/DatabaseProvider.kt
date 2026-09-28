package dev.koga.deeplinklauncher.domain.deeplink.impl.data.database

import app.cash.sqldelight.db.SqlDriver
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.Deeplink
import dev.koga.deeplinklauncher.database.DriverFactory
import dev.koga.deeplinklauncher.database.converter.localDateTimeAdapter

internal class DatabaseProvider(
    private val driverFactory: DriverFactory,
) {
    fun create(): DeepLinkLauncherDatabase = createDeepLinkLauncherDatabase(
        driverFactory.createDriver(schema = DeepLinkLauncherDatabase.Schema, databaseName = DATABASE_NAME),
    )

    private companion object {
        private const val DATABASE_NAME = "dll-db"
    }
}

internal fun createDeepLinkLauncherDatabase(driver: SqlDriver): DeepLinkLauncherDatabase =
    DeepLinkLauncherDatabase(
        driver = driver,
        deeplinkAdapter = Deeplink.Adapter(
            createdAtAdapter = localDateTimeAdapter,
            lastLaunchedAtAdapter = localDateTimeAdapter,
        ),
    )
