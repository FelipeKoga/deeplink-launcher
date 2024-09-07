package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import dev.koga.deeplinklauncher.model.DeepLink
import platform.Foundation.NSFileManager
import platform.Foundation.NSLibraryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.URLByAppendingPathComponent

internal class NativeDriverFactory : DriverFactory {

    override fun createDriver(databaseName: String): SqlDriver =
        NativeSqliteDriver(DeepLinkLauncherDatabase.Schema, databaseName)

    override fun shouldPrepopulateDatabase(databaseName: String): Boolean =
        !databaseExists(databaseName)

    override fun getPrepopulateData(): List<DeepLink> {
        return emptyList()
    }

    private fun databaseExists(databaseName: String): Boolean {
        val fileManager = NSFileManager.defaultManager
        val documentDirectory = NSFileManager
            .defaultManager
            .URLsForDirectory(
                NSLibraryDirectory,
                NSUserDomainMask,
            ).last() as NSURL

        val file = documentDirectory
            .URLByAppendingPathComponent("$DATABASE_PATH$databaseName")
            ?.path

        return fileManager.fileExistsAtPath(file ?: "")
    }

    private companion object {
        private const val DATABASE_PATH = "Application Support/databases/"
    }
}
