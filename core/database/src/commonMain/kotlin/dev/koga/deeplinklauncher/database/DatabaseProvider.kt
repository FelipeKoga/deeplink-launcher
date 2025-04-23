package dev.koga.deeplinklauncher.database

import dev.koga.deeplinklauncher.database.converter.localDateTimeAdapter

internal class DatabaseProvider(
    private val driverFactory: DriverFactory,
) {
    fun create(): DeepLinkLauncherDatabase {
        val driver = driverFactory.createDriver(databaseName = DATABASE_NAME)

        val database = DeepLinkLauncherDatabase(
            driver = driver,
            Deeplink.Adapter(
                createdAtAdapter = localDateTimeAdapter,
                lastLaunchedAtAdapter = localDateTimeAdapter,
            ),
        )

        return database
    }

    private companion object {
        private const val DATABASE_NAME = "dll-db"
    }
}
