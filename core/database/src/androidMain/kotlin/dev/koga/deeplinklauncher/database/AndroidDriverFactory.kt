package dev.koga.deeplinklauncher.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.koga.deeplinklauncher.constant.defaultDeepLink
import dev.koga.deeplinklauncher.model.DeepLink as DomainDeepLink

internal class AndroidDriverFactory(
    private val context: Context,
) : DriverFactory {

    override fun createDriver(databaseName: String): SqlDriver =
        AndroidSqliteDriver(DeepLinkLauncherDatabase.Schema, context, databaseName)

    override fun shouldPrepopulateDatabase(databaseName: String): Boolean =
        !context.getDatabasePath(databaseName).exists()

    override fun getPrepopulateData(): List<DomainDeepLink> = listOf(
        defaultDeepLink,
    )
}
