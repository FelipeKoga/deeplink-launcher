package dev.koga.deeplinklauncher.deeplink.impl.data.repository

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.Deeplink
import dev.koga.deeplinklauncher.database.converter.localDateTimeAdapter
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository.UpsertResult
import dev.koga.deeplinklauncher.deeplink.impl.data.repository.RepositoryFixture.Companion.deepLink
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties
import kotlin.concurrent.thread
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ConcurrentWriterTest {
    private val file = File.createTempFile("dll", ".db").apply { delete() }
    private val url = "jdbc:sqlite:${file.absolutePath}"
    private val driver = JdbcSqliteDriver(url, Properties(), DeepLinkLauncherDatabase.Schema)
    private val repository = DeepLinkRepositoryImpl(
        DeepLinkLauncherDatabase(driver, Deeplink.Adapter(localDateTimeAdapter, localDateTimeAdapter)),
    )
    private val writers = mutableListOf<Thread>()

    @AfterTest
    fun tearDown() {
        writers.forEach(Thread::join)
        driver.close()
        file.delete()
        File("${file.absolutePath}-journal").delete()
    }

    @Test
    fun waitsForAnotherWriterInsteadOfFailing() {
        val a = deepLink(id = "a", link = "myapp://a", folder = null)
        repository.upsertDeepLink(a)

        holdWriteLock { it.createStatement().execute("UPDATE deeplink SET name = 'Other' WHERE id = 'a'") }

        assertEquals(UpsertResult.Saved, repository.upsertDeepLink(a.copy(name = "Mine")))
    }

    @Test
    fun secondSubmitOfSameLinkSeesTheFirst() {
        holdWriteLock {
            it.createStatement().execute("INSERT INTO deeplink (id, link, createdAt) VALUES ('first', 'myapp://x', 0)")
        }

        val result = repository.upsertDeepLink(deepLink(id = "second", link = "myapp://x", folder = null))

        assertEquals(UpsertResult.LinkAlreadyExists("first"), result)
    }

    private fun holdWriteLock(write: (Connection) -> Unit) {
        val other = DriverManager.getConnection(url)
        other.createStatement().execute("BEGIN IMMEDIATE")
        write(other)
        writers += thread {
            Thread.sleep(WRITE_LOCK_MILLIS)
            other.createStatement().execute("COMMIT")
            other.close()
        }
    }

    private companion object {
        const val WRITE_LOCK_MILLIS = 300L
    }
}
