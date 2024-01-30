package dev.koga.deeplinklauncher.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.koga.deeplinklauncher.database.DatabaseProvider
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

internal class FolderDataSourceImpl(
    private val databaseProvider: DatabaseProvider,
) : FolderDataSource {

    private val database: DeepLinkLauncherDatabase
        get() = databaseProvider.getInstance()

    override fun getFoldersStream(): Flow<List<Folder>> {
        return database
            .deepLinkLauncherDatabaseQueries
            .selectFoldersWithDeeplinkCount()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { data ->
                data.map {
                    Folder(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        deepLinkCount = it.deeplinkCount.toInt(),
                    )
                }
            }
    }

    override fun getFolderDeepLinksStream(id: String): Flow<List<DeepLink>> {
        return database
            .deepLinkLauncherDatabaseQueries
            .getFolderDeepLinks(id)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map {
                it.map { data ->
                    DeepLink(
                        id = data.id,
                        link = data.link,
                        name = data.name,
                        description = data.description,
                        createdAt = Instant.fromEpochMilliseconds(data.createdAt),
                        isFavorite = data.isFavorite == 1L,
                    )
                }
            }
    }

    override fun getFolderById(id: String): Folder? {
        return database.deepLinkLauncherDatabaseQueries
            .getFolderById(id)
            .executeAsOne()
            .let {
                Folder(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    deepLinkCount = it.deeplinkCount.toInt(),
                )
            }
    }

    override fun upsertFolder(folder: Folder) {
        database.deepLinkLauncherDatabaseQueries.upsertFolder(
            id = folder.id,
            name = folder.name,
            description = folder.description,
        )
    }

    override fun deleteFolder(id: String) {
        database.transaction {
            database.deepLinkLauncherDatabaseQueries.removeFolderFromDeeplinks(id)
            database.deepLinkLauncherDatabaseQueries.deleteFolderById(id)
        }
    }
}
