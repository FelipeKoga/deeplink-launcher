package dev.koga.deeplinklauncher.deeplink.impl.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.GetDeepLinkByLink
import dev.koga.deeplinklauncher.database.SelectAllDeeplinks
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.impl.data.mapper.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime

internal class DeepLinkRepositoryImpl(
    private val database: DeepLinkLauncherDatabase,
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

    override fun upsertDeepLink(deepLink: DeepLink): DeepLinkRepository.UpsertResult =
        database.transactionWithResult {
            val queries = database.deepLinkQueries
            val isFavorite = if (deepLink.isFavorite) 1L else 0L
            val updatedRows = queries.updateDeeplink(
                link = deepLink.link,
                name = deepLink.name,
                description = deepLink.description,
                createdAt = deepLink.createdAt,
                lastLaunchedAt = deepLink.lastLaunchedAt,
                isFavorite = isFavorite,
                folderId = deepLink.folder?.id,
                targetPackage = deepLink.targetPackage,
                id = deepLink.id,
            ).value
            if (updatedRows > 0L) return@transactionWithResult DeepLinkRepository.UpsertResult.Saved

            val existingId = queries.selectOtherDeeplinkIdByLink(link = deepLink.link, id = deepLink.id)
                .executeAsOneOrNull()
            if (existingId != null) {
                return@transactionWithResult DeepLinkRepository.UpsertResult.LinkAlreadyExists(existingId)
            }

            queries.insertDeeplink(
                id = deepLink.id,
                link = deepLink.link,
                name = deepLink.name,
                description = deepLink.description,
                createdAt = deepLink.createdAt,
                lastLaunchedAt = deepLink.lastLaunchedAt,
                isFavorite = isFavorite,
                folderId = deepLink.folder?.id,
                targetPackage = deepLink.targetPackage,
            )
            DeepLinkRepository.UpsertResult.Saved
        }

    override fun updateLastLaunchedAt(id: String, lastLaunchedAt: LocalDateTime) {
        database.deepLinkQueries.updateLastLaunchedAt(lastLaunchedAt = lastLaunchedAt, id = id)
    }

    override fun deleteDeepLink(id: String) {
        database.deepLinkQueries.deleteDeeplinkById(id)
    }

    override fun deleteAll() {
        database.deepLinkQueries.deleteAllDeeplinks()
    }
}
