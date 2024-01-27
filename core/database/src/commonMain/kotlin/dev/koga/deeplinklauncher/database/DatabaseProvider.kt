package dev.koga.deeplinklauncher.database

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
        )

        prepopulateDatabase(database)
        return database
    }

    private fun prepopulateDatabase(database: DeepLinkLauncherDatabase) {
        if (driverFactory.shouldPrepopulateDatabase(DATABASE_NAME)) {
            driverFactory.getPrepopulateData().forEach { deeplink ->
                database.deepLinkLauncherDatabaseQueries.upsertDeeplink(
                    id = deeplink.id,
                    link = deeplink.link,
                    name = deeplink.name,
                    description = deeplink.description,
                    createdAt = deeplink.createdAt.toEpochMilliseconds(),
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
