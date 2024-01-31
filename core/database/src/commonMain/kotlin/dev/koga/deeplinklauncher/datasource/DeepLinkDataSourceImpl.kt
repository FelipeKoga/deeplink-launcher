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

internal class DeepLinkDataSourceImpl(
    private val databaseProvider: DatabaseProvider,
    private val folderDataSource: FolderDataSource,
) : DeepLinkDataSource {

    private val database: DeepLinkLauncherDatabase
        get() = databaseProvider.getInstance()

    override fun getDeepLinksStream(): Flow<List<DeepLink>> {
        return database.deepLinkQueries
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
                        createdAt = data.createdAt,
                        isFavorite = data.isFavorite == 1L,
                        folder = data.folderId?.let { folderId ->
                            Folder(
                                id = folderId,
                                name = data.name_.orEmpty(),
                                description = data.description_,
                            )
                        },
                    )
                }
            }
    }

    override fun getDeepLinks(): List<DeepLink> {
        return database.deepLinkQueries
            .selectAllDeeplinks()
            .executeAsList()
            .map { data ->
                DeepLink(
                    id = data.id,
                    link = data.link,
                    name = data.name,
                    description = data.description,
                    createdAt = data.createdAt,
                    isFavorite = data.isFavorite == 1L,
                    folder = data.folderId?.let { folderId ->
                        Folder(
                            id = folderId,
                            name = data.name_.orEmpty(),
                            description = data.description_,
                            deepLinkCount = 1,
                        )
                    },
                )
            }
    }

    override fun getDeepLinkById(id: String): DeepLink {
        return database.deepLinkQueries
            .getDeepLinkById(id)
            .executeAsOne()
            .let(GetDeepLinkById::toModel)
    }

    override fun getDeepLinkByLink(link: String): DeepLink? {
        return database.deepLinkQueries
            .getDeepLinkByLink(link)
            .executeAsOneOrNull()
            ?.let(GetDeepLinkByLink::toModel)
    }

    override fun upsertDeepLink(deepLink: DeepLink) {
        database.transaction {
            deepLink.folder?.let {
                folderDataSource.upsertFolder(it)
            }

            database.deepLinkQueries.upsertDeeplink(
                id = deepLink.id,
                link = deepLink.link,
                name = deepLink.name,
                description = deepLink.description,
                createdAt = deepLink.createdAt,
                isFavorite = if (deepLink.isFavorite) 1L else 0L,
                folderId = deepLink.folder?.id,
            )
        }
    }

    override fun deleteDeepLink(id: String) {
        database.transaction {
            database.deepLinkQueries.deleteDeeplinkById(id)
        }
    }
}

private fun GetDeepLinkById.toModel() = DeepLink(
    id = id,
    link = link,
    name = name,
    description = description,
    createdAt = createdAt,
    isFavorite = isFavorite == 1L,
    folder = folderId?.let { folderId ->
        Folder(
            id = folderId,
            name = name_.orEmpty(),
            description = description_,
        )
    },
)

private fun GetDeepLinkByLink.toModel() = DeepLink(
    id = id,
    link = link,
    name = name,
    description = description,
    createdAt = createdAt,
    isFavorite = isFavorite == 1L,
    folder = folderId?.let { folderId ->
        Folder(
            id = folderId,
            name = name_.orEmpty(),
            description = description_,
        )
    },
)
