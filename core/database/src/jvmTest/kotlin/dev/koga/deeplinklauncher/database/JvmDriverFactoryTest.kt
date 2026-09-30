package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.SqlDriver
import dev.koga.deeplinklauncher.database.converter.localDateTimeAdapter
import kotlinx.datetime.LocalDateTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.sql.DriverManager

class JvmDriverFactoryTest {

    private lateinit var tempDir: File
    private lateinit var databaseFile: File

    @Before
    fun setUp() {
        tempDir = Files.createTempDirectory("jvm-driver-factory-test").toFile()
        databaseFile = File(tempDir, "dll-db.db")
    }

    @After
    fun tearDown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun createsNewDatabaseAtCurrentSchemaVersion() {
        createJvmDriver(databaseFile).close()

        assertEquals(DeepLinkLauncherDatabase.Schema.version, userVersion())
    }

    @Test
    fun upgradesUnversionedDatabaseCreatedByOlderDesktopVersions() {
        createUnversionedDatabase()

        val driver = createJvmDriver(databaseFile)
        val deepLink = driver.database().deepLinkQueries.getDeepLinkById("legacy-id").executeAsOne()
        driver.close()

        assertEquals("myapp://legacy", deepLink.link)
        assertEquals("Legacy", deepLink.name)
        assertNull(deepLink.targetPackage)
        assertEquals(DeepLinkLauncherDatabase.Schema.version, userVersion())
    }

    @Test
    fun reopensDatabaseWithoutLosingData() {
        val firstDriver = createJvmDriver(databaseFile)
        firstDriver.database().deepLinkQueries.upsertDeeplink(
            id = "id",
            link = "myapp://home",
            name = null,
            description = null,
            createdAt = LocalDateTime(2026, 1, 15, 10, 30),
            lastLaunchedAt = null,
            isFavorite = 0,
            folderId = null,
            targetPackage = "com.example",
        )
        firstDriver.close()

        val secondDriver = createJvmDriver(databaseFile)
        val deepLink = secondDriver.database().deepLinkQueries.getDeepLinkById("id").executeAsOne()
        secondDriver.close()

        assertEquals("com.example", deepLink.targetPackage)
    }

    @Test
    fun upgradesEverySchemaSnapshotToCurrentVersion() {
        val snapshots = File("src/commonMain/sqldelight/databases")
            .listFiles { file -> file.extension == "db" }
            .orEmpty()
        assertTrue(snapshots.isNotEmpty())

        snapshots.forEach { snapshot ->
            val file = File(tempDir, snapshot.name)
            snapshot.copyTo(file)
            setUserVersion(file, snapshot.nameWithoutExtension.toInt())

            val driver = createJvmDriver(file)
            val database = driver.database()
            database.folderQueries.upsertFolder(id = "folder", name = "Folder", description = null)
            database.deepLinkQueries.upsertDeeplink(
                id = "id",
                link = "myapp://home",
                name = null,
                description = null,
                createdAt = LocalDateTime(2026, 1, 15, 10, 30),
                lastLaunchedAt = null,
                isFavorite = 0,
                folderId = "folder",
                targetPackage = "com.example",
            )
            val deepLinks = database.folderQueries.getFolderDeepLinks("folder").executeAsList()
            driver.close()

            assertEquals(snapshot.name, 1, deepLinks.size)
            assertEquals(snapshot.name, DeepLinkLauncherDatabase.Schema.version, userVersion(file))
        }
    }

    @Test
    fun relocatesLegacyDatabaseAndUpgradesIt() {
        val legacyFile = File(tempDir, "home/dll-db.db").apply { parentFile.mkdirs() }
        createUnversionedDatabase(legacyFile)

        relocateLegacyDatabase(legacyFile = legacyFile, databaseFile = databaseFile)
        val driver = createJvmDriver(databaseFile)
        val deepLink = driver.database().deepLinkQueries.getDeepLinkById("legacy-id").executeAsOne()
        driver.close()

        assertFalse(legacyFile.exists())
        assertEquals("myapp://legacy", deepLink.link)
    }

    @Test
    fun doesNotCreateLegacyDatabaseWhenItIsMissing() {
        val legacyFile = File(tempDir, "home/dll-db.db").apply { parentFile.mkdirs() }

        relocateLegacyDatabase(legacyFile = legacyFile, databaseFile = databaseFile)

        assertFalse(legacyFile.exists())
        assertFalse(databaseFile.exists())
    }

    @Test
    fun rollsBackInterruptedLegacyWriteBeforeRelocating() {
        val legacyFile = File(tempDir, "home/dll-db.db").apply { parentFile.mkdirs() }
        createUnversionedDatabase(legacyFile, rows = 2_000)
        leaveHotJournal(legacyFile)

        relocateLegacyDatabase(legacyFile = legacyFile, databaseFile = databaseFile)
        val driver = createJvmDriver(databaseFile)
        val deepLinks = driver.database().deepLinkQueries.selectAllDeeplinks().executeAsList()
        driver.close()

        assertEquals("ok", integrityCheck(databaseFile))
        assertEquals(2_000, deepLinks.size)
        assertTrue(deepLinks.all { it.description == null })
    }

    private fun leaveHotJournal(file: File) {
        val journal = File("${file.path}-journal")
        val crashedFile = File(tempDir, "crashed.db")
        val crashedJournal = File(tempDir, "crashed.db-journal")
        val originalBytes = file.readBytes()

        DriverManager.getConnection(url(file)).use { connection ->
            connection.createStatement().use { statement ->
                statement.execute("PRAGMA cache_size = 1")
                connection.autoCommit = false
                statement.executeUpdate("UPDATE deeplink SET description = hex(randomblob(64))")
                file.copyTo(crashedFile)
                journal.copyTo(crashedJournal)
                connection.rollback()
            }
        }

        assertNotEquals(originalBytes.toList(), crashedFile.readBytes().toList())
        crashedFile.copyTo(file, overwrite = true)
        crashedJournal.copyTo(journal, overwrite = true)
    }

    private fun createUnversionedDatabase(file: File = databaseFile, rows: Int = 0) {
        DriverManager.getConnection(url(file)).use { connection ->
            connection.createStatement().use { statement ->
                statement.executeUpdate(
                    """
                    CREATE TABLE folder (
                        id TEXT PRIMARY KEY,
                        name TEXT UNIQUE NOT NULL,
                        description TEXT
                    )
                    """.trimIndent(),
                )
                statement.executeUpdate(
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
                statement.executeUpdate(
                    "INSERT INTO deeplink (id, link, name, createdAt) " +
                        "VALUES ('legacy-id', 'myapp://legacy', 'Legacy', 0)",
                )
                repeat(rows - 1) { index ->
                    statement.executeUpdate(
                        "INSERT INTO deeplink (id, link, createdAt) VALUES ('id-$index', 'myapp://$index', 0)",
                    )
                }
            }
        }
    }

    private fun setUserVersion(file: File, version: Int) {
        DriverManager.getConnection(url(file)).use { connection ->
            connection.createStatement().use { it.executeUpdate("PRAGMA user_version = $version") }
        }
    }

    private fun integrityCheck(file: File): String = DriverManager.getConnection(url(file)).use { connection ->
        connection.createStatement().use { statement ->
            statement.executeQuery("PRAGMA integrity_check").use { result ->
                result.next()
                result.getString(1)
            }
        }
    }

    private fun userVersion(file: File = databaseFile): Long = DriverManager.getConnection(url(file)).use { connection ->
        connection.createStatement().use { statement ->
            statement.executeQuery("PRAGMA user_version").use { it.getLong(1) }
        }
    }

    private fun url(file: File = databaseFile) = "jdbc:sqlite:${file.absolutePath}"

    private fun SqlDriver.database() = DeepLinkLauncherDatabase(
        driver = this,
        deeplinkAdapter = Deeplink.Adapter(
            createdAtAdapter = localDateTimeAdapter,
            lastLaunchedAtAdapter = localDateTimeAdapter,
        ),
    )
}
