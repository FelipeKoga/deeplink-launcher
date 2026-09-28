package dev.koga.deeplinklauncher.domain.deeplink.impl.data.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.domain.deeplink.impl.data.repository.DeepLinkRepositoryImpl
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The schema moved from :core:database to this module. Existing installs must keep
 * the same schema version and migrate exactly as before.
 */
class SchemaMigrationTest {

    @Test
    fun `schema version is unchanged by the move`() {
        assertEquals(2L, DeepLinkLauncherDatabase.Schema.version)
    }

    @Test
    fun `a version 1 database migrates and keeps its rows`() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        // Tables as shipped in schema version 1 (before targetPackage).
        driver.execute(null, "CREATE TABLE folder (id TEXT PRIMARY KEY, name TEXT UNIQUE NOT NULL, description TEXT)", 0)
        driver.execute(
            null,
            """
            CREATE TABLE deeplink(
              id TEXT PRIMARY KEY, link TEXT NOT NULL UNIQUE, name TEXT, description TEXT,
              createdAt INTEGER NOT NULL, lastLaunchedAt INTEGER, folderId TEXT, isFavorite INTEGER DEFAULT 0,
              FOREIGN KEY(folderId) REFERENCES folder(id) ON UPDATE CASCADE
            )
            """.trimIndent(),
            0,
        )
        driver.execute(null, "INSERT INTO deeplink (id, link, createdAt) VALUES ('a', 'demo://one', 0)", 0)

        DeepLinkLauncherDatabase.Schema.migrate(driver, oldVersion = 1, newVersion = DeepLinkLauncherDatabase.Schema.version)

        val repository = DeepLinkRepositoryImpl(createDeepLinkLauncherDatabase(driver))
        val migrated = repository.getDeepLinkById("a")
        assertEquals("demo://one", migrated?.link)
        assertEquals(null, migrated?.targetPackage)
    }
}
