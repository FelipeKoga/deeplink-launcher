package dev.koga.deeplinklauncher.database

import dev.koga.deeplinklauncher.converter.localDateTimeAdapter

internal class DatabaseProvider(
    private val driverFactory: DriverFactory,
) {

    private var database: DeepLinkLauncherDatabase? = null

    fun getInstance(): DeepLinkLauncherDatabase =
        database ?: createDatabase().also { database = it }

    private fun createDatabase(): DeepLinkLauncherDatabase {
        val driver = driverFactory.createDriver(databaseName = DATABASE_NAME)

        val database = DeepLinkLauncherDatabase(
            driver = driver,
            Deeplink.Adapter(
                createdAtAdapter = localDateTimeAdapter,
                lastLaunchedAtAdapter = localDateTimeAdapter,
            )
        )

        prepopulateDatabase(database)
        return database
    }

    private fun prepopulateDatabase(database: DeepLinkLauncherDatabase) {
        if (driverFactory.shouldPrepopulateDatabase(DATABASE_NAME)) {
            driverFactory.getPrepopulateData().forEach { deeplink ->
                database.deepLinkQueries.upsertDeeplink(
                    id = deeplink.id,
                    link = deeplink.link,
                    name = deeplink.name,
                    description = deeplink.description,
                    createdAt = deeplink.createdAt,
                    lastLaunchedAt = null,
                    isFavorite = deeplink.isFavorite.let { if (it) 1L else 0L },
                    folderId = deeplink.folder?.id,
                )
            }
        }
    }

    private companion object {
        private const val DATABASE_NAME = "dll-db"
    }
}
