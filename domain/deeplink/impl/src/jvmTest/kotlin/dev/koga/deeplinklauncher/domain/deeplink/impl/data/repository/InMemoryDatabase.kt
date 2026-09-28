package dev.koga.deeplinklauncher.domain.deeplink.impl.data.repository
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.domain.deeplink.impl.data.database.createDeepLinkLauncherDatabase

internal class InMemoryDatabase {
    val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
        DeepLinkLauncherDatabase.Schema.create(it)
    }
    val database: DeepLinkLauncherDatabase = createDeepLinkLauncherDatabase(driver)

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
