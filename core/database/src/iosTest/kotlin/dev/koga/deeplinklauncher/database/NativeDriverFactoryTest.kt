package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import co.touchlab.sqliter.DatabaseConfiguration
import co.touchlab.sqliter.DatabaseFileContext
import co.touchlab.sqliter.createDatabaseManager
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NativeDriverFactoryTest {

    private val drivers = mutableListOf<SqlDriver>()

    @BeforeTest
    fun setUp() {
        DatabaseFileContext.deleteDatabase(DATABASE_NAME)
    }

    @AfterTest
    fun tearDown() {
        drivers.forEach { it.close() }
        DatabaseFileContext.deleteDatabase(DATABASE_NAME)
    }

    @Test
    fun createsNewDatabaseInApplicationSupportAtCurrentSchemaVersion() {
        openDatabase().deepLinkQueries.selectAllDeeplinks().executeAsList()
        val version = drivers.single().userVersion()

        assertEquals(DeepLinkLauncherDatabase.Schema.version, version)
        assertTrue(NSFileManager.defaultManager.fileExistsAtPath(expectedDatabasePath()))
    }

    @Test
    fun upgradesDatabaseCreatedByVersion1() {
        createVersion1Database()

        val deepLink = openDatabase().deepLinkQueries.getDeepLinkById("legacy-id").executeAsOne()
        val version = drivers.single().userVersion()

        assertEquals("myapp://legacy", deepLink.link)
        assertEquals("Legacy", deepLink.name)
        assertNull(deepLink.targetPackage)
        assertEquals(DeepLinkLauncherDatabase.Schema.version, version)
    }

    private fun createVersion1Database() {
        val manager = createDatabaseManager(
            DatabaseConfiguration(
                name = DATABASE_NAME,
                version = 1,
                create = { connection ->
                    connection.rawExecSql(
                        """
                        CREATE TABLE folder (
                            id TEXT PRIMARY KEY,
                            name TEXT UNIQUE NOT NULL,
                            description TEXT
                        )
                        """.trimIndent(),
                    )
                    connection.rawExecSql(
                        """
                        CREATE TABLE deeplink(
                          id TEXT PRIMARY KEY,
                          link TEXT NOT NULL UNIQUE,
                          name TEXT,
                          description TEXT,
                          createdAt INTEGER NOT NULL,
                          lastLaunchedAt INTEGER,
                          folderId TEXT,
                          isFavorite INTEGER DEFAULT 0,
                          FOREIGN KEY(folderId) REFERENCES folder(id) ON UPDATE CASCADE
                        )
                        """.trimIndent(),
                    )
                    connection.rawExecSql(
                        "INSERT INTO deeplink (id, link, name, createdAt) VALUES ('legacy-id', 'myapp://legacy', 'Legacy', 0)",
                    )
                },
            ),
        )
        manager.createMultiThreadedConnection().close()
    }

    private fun expectedDatabasePath(): String {
        val applicationSupport = NSSearchPathForDirectoriesInDomains(NSApplicationSupportDirectory, NSUserDomainMask, true)
            .first() as String
        return "$applicationSupport/databases/$DATABASE_NAME"
    }

    private fun SqlDriver.userVersion(): Long = executeQuery(
        identifier = null,
        sql = "PRAGMA user_version",
        mapper = { cursor -> QueryResult.Value(if (cursor.next().value) cursor.getLong(0) else null) },
        parameters = 0,
    ).value ?: 0L

    private fun openDatabase(): DeepLinkLauncherDatabase {
        val driverFactory = object : DriverFactory {
            override fun createDriver(databaseName: String): SqlDriver =
                NativeDriverFactory().createDriver(databaseName).also { drivers += it }
        }
        return DatabaseProvider(driverFactory).create()
    }

    private companion object {
        const val DATABASE_NAME = "dll-db"
    }
}
