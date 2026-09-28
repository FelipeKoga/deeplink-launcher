package dev.koga.deeplinklauncher.deeplink.impl.data.repository

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.createDeepLinkLauncherDatabase

/**
 * Fresh in-memory database with production adapters. Foreign keys stay disabled,
 * as on every production driver.
 */
internal class InMemoryDatabase {
    val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
        DeepLinkLauncherDatabase.Schema.create(it)
    }
    val database: DeepLinkLauncherDatabase = createDeepLinkLauncherDatabase(driver)

    /** Makes any insert of [link] fail, to simulate a database error in the middle of a write. */
    fun failInsertsOf(link: String) {
        driver.execute(
            identifier = null,
            sql = """
                CREATE TRIGGER fail_insert BEFORE INSERT ON deeplink
                WHEN NEW.link = '$link'
                BEGIN SELECT RAISE(ABORT, 'simulated failure'); END;
            """.trimIndent(),
            parameters = 0,
        )
    }
}
