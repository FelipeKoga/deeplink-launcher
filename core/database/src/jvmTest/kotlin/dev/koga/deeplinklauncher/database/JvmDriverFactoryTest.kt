package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.db.SqlDriver
import dev.koga.deeplinklauncher.database.converter.localDateTimeAdapter
import kotlinx.datetime.LocalDateTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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

    private fun createUnversionedDatabase() {
        DriverManager.getConnection(url()).use { connection ->
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
            }
        }
    }

    private fun userVersion(): Long = DriverManager.getConnection(url()).use { connection ->
        connection.createStatement().use { statement ->
            statement.executeQuery("PRAGMA user_version").use { it.getLong(1) }
        }
    }

    private fun url() = "jdbc:sqlite:${databaseFile.absolutePath}"

    private fun SqlDriver.database() = DeepLinkLauncherDatabase(
        driver = this,
        deeplinkAdapter = Deeplink.Adapter(
            createdAtAdapter = localDateTimeAdapter,
            lastLaunchedAtAdapter = localDateTimeAdapter,
        ),
    )
}
