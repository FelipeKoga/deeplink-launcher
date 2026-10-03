package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import co.touchlab.sqliter.DatabaseFileContext
import co.touchlab.sqliter.createDatabaseManager
import co.touchlab.sqliter.interop.Logger
import dev.koga.deeplinklauncher.database.converter.localDateTimeAdapter
import kotlinx.datetime.LocalDateTime
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
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

    @Test
    fun updateDeeplinkCountsOnlyTheRowItChanges() {
        val queries = openQuietDatabase().deepLinkQueries
        queries.insertLink(id = "a", link = "myapp://a")
        queries.insertLink(id = "b", link = "myapp://b")

        assertEquals(1L, queries.updateLink(id = "a", link = "myapp://a"))
        assertEquals(0L, queries.updateLink(id = "missing", link = "myapp://missing"))
        assertEquals(0L, queries.updateLink(id = "a", link = "myapp://b"))
        assertFails { queries.insertLink(id = "c", link = "myapp://b") }
        assertEquals(
            listOf("a" to "myapp://a", "b" to "myapp://b"),
            queries.selectAllDeeplinks().executeAsList().map { it.id to it.link }.sortedBy { it.first },
        )
    }

    private fun DeepLinkQueries.insertLink(id: String, link: String) {
        insertDeeplink(
            id = id,
            link = link,
            name = null,
            description = null,
            createdAt = LocalDateTime(2026, 1, 15, 10, 30),
            lastLaunchedAt = null,
            isFavorite = 0,
            folderId = null,
            targetPackage = null,
        )
    }

    private fun DeepLinkQueries.updateLink(id: String, link: String): Long = updateDeeplink(
        link = link,
        name = null,
        description = null,
        createdAt = LocalDateTime(2026, 1, 15, 10, 30),
        lastLaunchedAt = null,
        isFavorite = 0,
        folderId = null,
        targetPackage = null,
        id = id,
    ).value

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

    private fun openQuietDatabase(): DeepLinkLauncherDatabase {
        val driver = NativeSqliteDriver(
            schema = DeepLinkLauncherDatabase.Schema,
            name = DATABASE_NAME,
            onConfiguration = { it.copy(loggingConfig = DatabaseConfiguration.Logging(logger = SilentLogger)) },
        ).also { drivers += it }
        return DeepLinkLauncherDatabase(
            driver = driver,
            deeplinkAdapter = Deeplink.Adapter(
                createdAtAdapter = localDateTimeAdapter,
                lastLaunchedAtAdapter = localDateTimeAdapter,
            ),
        )
    }

    private object SilentLogger : Logger {
        override val vActive: Boolean = false
        override val eActive: Boolean = false
        override fun trace(message: String) = Unit
        override fun vWrite(message: String) = Unit
        override fun eWrite(message: String, exception: Throwable?) = Unit
    }

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
