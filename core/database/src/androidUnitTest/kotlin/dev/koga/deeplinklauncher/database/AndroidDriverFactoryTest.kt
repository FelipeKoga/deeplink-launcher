package dev.koga.deeplinklauncher.database

import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import kotlinx.datetime.LocalDateTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class AndroidDriverFactoryTest {

    private lateinit var context: Context
    private val drivers = mutableListOf<SqlDriver>()

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        context.deleteDatabase(DATABASE_NAME)
    }

    @After
    fun tearDown() {
        closeDrivers()
        context.deleteDatabase(DATABASE_NAME)
    }

    @Test
    fun createsNewDatabaseAtCurrentSchemaVersion() {
        openDatabase().deepLinkQueries.selectAllDeeplinks().executeAsList()

        assertEquals(DeepLinkLauncherDatabase.Schema.version, userVersion())
    }

    @Test
    fun upgradesDatabaseCreatedByVersion1() {
        createVersion1Database()

        val deepLink = openDatabase().deepLinkQueries.getDeepLinkById("legacy-id").executeAsOne()

        assertEquals("myapp://legacy", deepLink.link)
        assertEquals("Legacy", deepLink.name)
        assertNull(deepLink.targetPackage)
        assertEquals(DeepLinkLauncherDatabase.Schema.version, userVersion())
    }

    @Test
    fun upgradesEverySchemaSnapshotKeepingItsRows() {
        val snapshots = File("src/commonMain/sqldelight/databases")
            .listFiles { file -> file.extension == "db" }
            .orEmpty()
        assertTrue(snapshots.isNotEmpty())

        snapshots.forEach { snapshot ->
            closeDrivers()
            context.deleteDatabase(DATABASE_NAME)
            snapshot.copyTo(databaseFile().apply { parentFile?.mkdirs() })
            withDatabaseFile { database ->
                database.execSQL("INSERT INTO folder (id, name) VALUES ('folder', 'Folder')")
                database.execSQL(
                    "INSERT INTO deeplink (id, link, createdAt, folderId) VALUES ('old', 'myapp://old', 0, 'folder')",
                )
                database.version = snapshot.nameWithoutExtension.toInt()
            }

            val database = openDatabase()
            database.deepLinkQueries.upsertDeeplink(
                id = "new",
                link = "myapp://new",
                name = null,
                description = null,
                createdAt = LocalDateTime(2026, 1, 15, 10, 30),
                lastLaunchedAt = null,
                isFavorite = 0,
                folderId = "folder",
                targetPackage = "com.example",
            )
            val folderDeepLinks = database.folderQueries.getFolderDeepLinks("folder").executeAsList()

            assertEquals(snapshot.name, setOf("old", "new"), folderDeepLinks.map { it.id }.toSet())
            assertEquals(snapshot.name, DeepLinkLauncherDatabase.Schema.version, userVersion())
        }
    }

    @Test
    fun keepsTheFileWhenItsVersionIsNewerThanTheSchema() {
        createVersion1Database()
        withDatabaseFile { it.version = DeepLinkLauncherDatabase.Schema.version.toInt() + 1 }

        assertThrows(RuntimeException::class.java) {
            openDatabase().deepLinkQueries.selectAllDeeplinks().executeAsList()
        }

        var rows = 0L
        withDatabaseFile { rows = DatabaseUtils.queryNumEntries(it, "deeplink") }
        assertEquals(1L, rows)
    }

    private fun openDatabase(): DeepLinkLauncherDatabase {
        val driverFactory = object : DriverFactory {
            override fun createDriver(databaseName: String): SqlDriver =
                AndroidDriverFactory(context).createDriver(databaseName).also { drivers += it }
        }
        return DatabaseProvider(driverFactory).create()
    }

    private fun closeDrivers() {
        drivers.forEach { it.close() }
        drivers.clear()
    }

    private fun createVersion1Database() {
        databaseFile().parentFile?.mkdirs()
        withDatabaseFile { database ->
            database.execSQL(
                """
                CREATE TABLE folder (
                    id TEXT PRIMARY KEY,
                    name TEXT UNIQUE NOT NULL,
                    description TEXT
                )
                """.trimIndent(),
            )
            database.execSQL(
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
            database.execSQL(
                "INSERT INTO deeplink (id, link, name, createdAt) VALUES ('legacy-id', 'myapp://legacy', 'Legacy', 0)",
            )
            database.version = 1
        }
    }

    private fun userVersion(): Long {
        var version = 0L
        withDatabaseFile { version = it.version.toLong() }
        return version
    }

    private fun withDatabaseFile(block: (SQLiteDatabase) -> Unit) {
        SQLiteDatabase.openOrCreateDatabase(databaseFile(), null).use(block)
    }

    private fun databaseFile(): File = context.getDatabasePath(DATABASE_NAME)

    private companion object {
        const val DATABASE_NAME = "dll-db"
    }
}
