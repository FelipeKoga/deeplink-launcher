package dev.koga.deeplinklauncher.deeplink.impl.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.GetDeepLinkByLink
import dev.koga.deeplinklauncher.database.SelectAllDeeplinks
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.repository.FolderRepository
import dev.koga.deeplinklauncher.deeplink.impl.mapper.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DeepLinkRepositoryImpl(
    private val database: DeepLinkLauncherDatabase,
    private val folderRepository: FolderRepository,
) : DeepLinkRepository {

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

    override fun getDeepLinkByIdStream(id: String): Flow<DeepLink?> {
        return database.deepLinkQueries
            .getDeepLinkById(id)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.singleOrNull()?.toDomain() }
    }

    override fun getDeepLinkById(id: String): DeepLink? {
        return database.deepLinkQueries
            .getDeepLinkById(id)
            .executeAsOneOrNull()
            ?.toDomain()
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
                folderRepository.upsertFolder(it)
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
