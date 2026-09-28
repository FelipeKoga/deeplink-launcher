package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.SqlDriver
import dev.koga.deeplinklauncher.database.converter.localDateTimeAdapter

internal class DatabaseProvider(
    private val driverFactory: DriverFactory,
) {
    fun create(): DeepLinkLauncherDatabase =
        createDeepLinkLauncherDatabase(driverFactory.createDriver(databaseName = DATABASE_NAME))

    private companion object {
        private const val DATABASE_NAME = "dll-db"
    }
}

/**
 * Wraps [driver] with the column adapters used in production. Tests use it with an
 * in-memory driver so they exercise the same adapters as the app.
 */
fun createDeepLinkLauncherDatabase(driver: SqlDriver): DeepLinkLauncherDatabase =
    DeepLinkLauncherDatabase(
        driver = driver,
        deeplinkAdapter = Deeplink.Adapter(
            createdAtAdapter = localDateTimeAdapter,
            lastLaunchedAtAdapter = localDateTimeAdapter,
        ),
    )
