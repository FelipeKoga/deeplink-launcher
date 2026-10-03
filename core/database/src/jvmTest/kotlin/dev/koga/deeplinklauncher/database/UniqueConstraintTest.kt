package dev.koga.deeplinklauncher.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.koga.deeplinklauncher.database.converter.localDateTimeAdapter
import kotlinx.datetime.LocalDateTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import java.sql.SQLException
import java.util.Properties

class UniqueConstraintTest {

    private val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY, Properties(), DeepLinkLauncherDatabase.Schema)
    private val database = DeepLinkLauncherDatabase(
        driver = driver,
        deeplinkAdapter = Deeplink.Adapter(
            createdAtAdapter = localDateTimeAdapter,
            lastLaunchedAtAdapter = localDateTimeAdapter,
        ),
    )

    @After
    fun tearDown() {
        driver.close()
    }

    @Test
    fun insertDeeplinkWithTakenLinkThrowsAndKeepsRow() {
        insertDeepLink(id = "a", link = "myapp://a", folderId = "f")

        val error = assertThrows(SQLException::class.java) {
            insertDeepLink(id = "b", link = "myapp://a", folderId = null)
        }

        assertTrue(error.message.orEmpty().contains("UNIQUE constraint failed: deeplink.link"))
        assertEquals(listOf(Triple("a", "myapp://a", "f")), deepLinkRows())
    }

    @Test
    fun updateDeeplinkToTakenLinkUpdatesNoRowAndKeepsBoth() {
        insertDeepLink(id = "a", link = "myapp://a", folderId = "f")
        insertDeepLink(id = "b", link = "myapp://b", folderId = "g")

        val updatedRows = database.deepLinkQueries.updateDeeplink(
            link = "myapp://b",
            name = "A",
            description = null,
            createdAt = CREATED_AT,
            lastLaunchedAt = null,
            isFavorite = 1,
            folderId = null,
            targetPackage = null,
            id = "a",
        ).value

        assertEquals(0L, updatedRows)
        assertEquals(listOf(Triple("a", "myapp://a", "f"), Triple("b", "myapp://b", "g")), deepLinkRows())
    }

    @Test
    fun insertFolderWithTakenNameThrowsAndKeepsRow() {
        database.folderQueries.insertFolder(id = "f", name = "Work", description = "Mine")
        insertDeepLink(id = "a", link = "myapp://a", folderId = "f")

        val error = assertThrows(SQLException::class.java) {
            database.folderQueries.insertFolder(id = "g", name = "Work", description = null)
        }

        assertTrue(error.message.orEmpty().contains("UNIQUE constraint failed: folder.name"))
        assertEquals(listOf(Triple("f", "Work", "Mine")), folderRows())
        assertEquals(listOf("a"), database.folderQueries.getFolderDeepLinks("f").executeAsList().map { it.id })
    }

    @Test
    fun updateFolderToTakenNameUpdatesNoRowAndKeepsBoth() {
        database.folderQueries.insertFolder(id = "f", name = "Work", description = null)
        database.folderQueries.insertFolder(id = "g", name = "Personal", description = "Home")
        insertDeepLink(id = "b", link = "myapp://b", folderId = "g")

        val updatedRows = database.folderQueries.updateFolder(name = "Personal", description = null, id = "f").value

        assertEquals(0L, updatedRows)
        assertEquals(listOf(Triple("f", "Work", null), Triple("g", "Personal", "Home")), folderRows())
        assertEquals(listOf("b"), database.folderQueries.getFolderDeepLinks("g").executeAsList().map { it.id })
    }

    private fun insertDeepLink(id: String, link: String, folderId: String?) {
        database.deepLinkQueries.insertDeeplink(
            id = id,
            link = link,
            name = null,
            description = null,
            createdAt = CREATED_AT,
            lastLaunchedAt = null,
            isFavorite = 0,
            folderId = folderId,
            targetPackage = null,
        )
    }

    private fun deepLinkRows() = database.deepLinkQueries.selectAllDeeplinks().executeAsList()
        .map { Triple(it.id, it.link, it.folderId) }
        .sortedBy { it.first }

    private fun folderRows() = database.folderQueries.selectFoldersWithDeeplinkCount().executeAsList()
        .map { Triple(it.id, it.name, it.description) }
        .sortedBy { it.first }

    private companion object {
        val CREATED_AT = LocalDateTime(2026, 1, 15, 10, 30)
    }
}
