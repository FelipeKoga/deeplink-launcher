package dev.koga.deeplinklauncher.deeplink.impl.data.repository

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.Deeplink
import dev.koga.deeplinklauncher.database.converter.localDateTimeAdapter
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import kotlinx.datetime.LocalDateTime
import java.util.Properties

internal class RepositoryFixture : AutoCloseable {
    private val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY, Properties(), DeepLinkLauncherDatabase.Schema)
    private val database = DeepLinkLauncherDatabase(
        driver = driver,
        deeplinkAdapter = Deeplink.Adapter(
            createdAtAdapter = localDateTimeAdapter,
            lastLaunchedAtAdapter = localDateTimeAdapter,
        ),
    )

    val folders = FolderRepositoryImpl(database)
    val deepLinks = DeepLinkRepositoryImpl(database)

    val work = Folder(id = "f", name = "Work", description = null)
    val personal = Folder(id = "g", name = "Personal", description = "Home")
    val a = deepLink(id = "a", link = "myapp://a", folder = work)
    val b = deepLink(id = "b", link = "myapp://b", folder = personal, isFavorite = true)
    val c = deepLink(id = "c", link = "myapp://c", folder = null)

    init {
        listOf(work, personal).forEach {
            database.folderQueries.insertFolder(id = it.id, name = it.name, description = it.description)
        }
        listOf(a, b, c).forEach(::insert)
    }

    fun insert(deepLink: DeepLink) {
        database.deepLinkQueries.insertDeeplink(
            id = deepLink.id,
            link = deepLink.link,
            name = deepLink.name,
            description = deepLink.description,
            createdAt = deepLink.createdAt,
            lastLaunchedAt = deepLink.lastLaunchedAt,
            isFavorite = if (deepLink.isFavorite) 1L else 0L,
            folderId = deepLink.folder?.id,
            targetPackage = deepLink.targetPackage,
        )
    }

    fun sortedFolders(): List<Folder> = folders.getFolders().sortedBy { it.id }

    override fun close() {
        driver.close()
    }

    companion object {
        fun deepLink(
            id: String,
            link: String,
            folder: Folder?,
            isFavorite: Boolean = false,
        ) = DeepLink(
            id = id,
            link = link,
            name = id.uppercase(),
            description = null,
            createdAt = LocalDateTime(2026, 1, 15, 10, 30),
            isFavorite = isFavorite,
            folder = folder,
        )
    }
}
