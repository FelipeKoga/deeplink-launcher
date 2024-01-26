package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.SqlDriver
import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import kotlinx.datetime.Clock
import dev.koga.deeplinklauncher.model.DeepLink as DomainDeepLink

internal class AndroidDriverFactory(
    private val context: Context,
) : DriverFactory {

    override fun createDriver(databaseName: String): SqlDriver =
        AndroidSqliteDriver(DeepLinkLauncherDatabase.Schema, context, databaseName)

    override fun shouldPrepopulateDatabase(databaseName: String): Boolean =
        !context.getDatabasePath(databaseName).exists()

    override fun getPrepopulateData(): List<DomainDeepLink> = listOf(
        DomainDeepLink(
            id = "1",
            link = "https://github.com/FelipeKoga/deeplink-launcher",
            name = "Thanks for trying out DeepLink Launcher!",
            description = "If you like this app, leave a star on my GitHub repository and on the PlayStore!",
            createdAt = Clock.System.now(),
            isFavorite = false,
            folder = null,
        ),
    )
}
