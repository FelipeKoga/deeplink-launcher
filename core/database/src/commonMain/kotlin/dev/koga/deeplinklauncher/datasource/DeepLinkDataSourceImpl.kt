package dev.koga.deeplinklauncher.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.koga.deeplinklauncher.database.DatabaseProvider
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.GetDeepLinkById
import dev.koga.deeplinklauncher.database.GetDeepLinkByLink
import dev.koga.deeplinklauncher.database.SelectAllDeeplinks
import dev.koga.deeplinklauncher.mapper.toDomain
import dev.koga.deeplinklauncher.model.DeepLink
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
            .map { it.map(SelectAllDeeplinks::toDomain) }
    }

    override fun getDeepLinks(): List<DeepLink> {
        return database.deepLinkQueries
            .selectAllDeeplinks()
            .executeAsList()
            .map(SelectAllDeeplinks::toDomain)
    }

    override fun getDeepLinkById(id: String): DeepLink {
        return database.deepLinkQueries
            .getDeepLinkById(id)
            .executeAsOne()
            .let(GetDeepLinkById::toDomain)
    }

    override fun getDeepLinkByLink(link: String): DeepLink? {
        return database.deepLinkQueries
            .getDeepLinkByLink(link)
            .executeAsOneOrNull()
            ?.let(GetDeepLinkByLink::toDomain)
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
                lastLaunchedAt = deepLink.lastLaunchedAt,
                folderId = deepLink.folder?.id,
            )
        }
    }

    override fun deleteDeepLink(id: String) {
        database.deepLinkQueries.deleteDeeplinkById(id)
    }

    override fun deleteAll() {
        database.deepLinkQueries.deleteAllDeeplinks()
    }
}
