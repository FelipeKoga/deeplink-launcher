package dev.koga.deeplinklauncher.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.koga.deeplinklauncher.database.DatabaseProvider
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.GetDeepLinkById
import dev.koga.deeplinklauncher.database.GetDeepLinkByLink
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

internal class DeepLinkDataSourceImpl(
    private val databaseProvider: DatabaseProvider
) : DeepLinkDataSource {

    private val database: DeepLinkLauncherDatabase
        get() = databaseProvider.getInstance()

    override fun getDeepLinksStream(): Flow<List<DeepLink>> {
        return database.deepLinkLauncherDatabaseQueries
            .selectAllDeeplinks()
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
                        folder = data.folderId?.let { folderId ->
                            Folder(
                                id = folderId,
                                name = data.name_.orEmpty(),
                                description = data.description_,
                                deepLinkCount = 1
                            )
                        }
                    )
                }
            }
    }

    override fun getDeepLinks(): List<DeepLink> {
        return database.deepLinkLauncherDatabaseQueries
            .selectAllDeeplinks()
            .executeAsList()
            .map { data ->
                DeepLink(
                    id = data.id,
                    link = data.link,
                    name = data.name,
                    description = data.description,
                    createdAt = Instant.fromEpochMilliseconds(data.createdAt),
                    isFavorite = data.isFavorite == 1L,
                    folder = data.folderId?.let { folderId ->
                        Folder(
                            id = folderId,
                            name = data.name_.orEmpty(),
                            description = data.description_,
                            deepLinkCount = 1
                        )
                    }
                )
            }
    }

    override fun getDeepLinkById(id: String): DeepLink? {
        return database.deepLinkLauncherDatabaseQueries
            .getDeepLinkById(id)
            .executeAsOne()
            .let(GetDeepLinkById::toModel)
    }

    override fun getDeepLinkByLink(link: String): DeepLink? {
        return database.deepLinkLauncherDatabaseQueries
            .getDeepLinkByLink(link)
            .executeAsOneOrNull()
            ?.let(GetDeepLinkByLink::toModel)
    }

    override fun upsertDeepLink(deepLink: DeepLink) {
        database.transaction {
            deepLink.folder?.let { folder ->
                database.deepLinkLauncherDatabaseQueries.upsertFolder(
                    id = folder.id,
                    name = folder.name,
                    description = folder.description,
                )
            }

            database.deepLinkLauncherDatabaseQueries.upsertDeeplink(
                id = deepLink.id,
                link = deepLink.link,
                name = deepLink.name,
                description = deepLink.description,
                createdAt = deepLink.createdAt.toEpochMilliseconds(),
                isFavorite = if (deepLink.isFavorite) 1L else 0L,
                folderId = deepLink.folder?.id
            )
        }
    }

    override fun deleteDeepLink(id: String) {
        database.transaction {
            database.deepLinkLauncherDatabaseQueries.deleteDeeplinkById(id)
        }
    }
}


fun GetDeepLinkById.toModel() = DeepLink(
    id = id,
    link = link,
    name = name,
    description = description,
    createdAt = Instant.fromEpochMilliseconds(createdAt),
    isFavorite = isFavorite == 1L,
    folder = folderId?.let { folderId ->
        Folder(
            id = folderId,
            name = name_.orEmpty(),
            description = description_,
            deepLinkCount = 1
        )
    }
)

fun GetDeepLinkByLink.toModel() = DeepLink(
    id = id,
    link = link,
    name = name,
    description = description,
    createdAt = Instant.fromEpochMilliseconds(createdAt),
    isFavorite = isFavorite == 1L,
    folder = folderId?.let { folderId ->
        Folder(
            id = folderId,
            name = name_.orEmpty(),
            description = description_,
            deepLinkCount = 1
        )
    }
)