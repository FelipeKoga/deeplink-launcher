package dev.koga.deeplinklauncher.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DeepLinkRepository(
    private val database: DeepLinkLauncherDatabase
) {

    fun getAllDeepLinks(): Flow<List<DeepLink>> {
        return database.deepLinkLauncherDatabaseQueries
            .selectAllDeepLinks()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map {
                it.map {
                    DeepLink(
                        id = it.id,
                        link = it.link,
                        name = it.name,
                        description = it.description,
                        createdAt = it.createdAt,
                        updatedAt = it.updatedAt,
                        folder = it.folderId?.let {  folderId ->
                            Folder(
                                id = folderId,
                                name = it.name_,
                                description = it.description_,
                                color = it.color
                            )
                        }
                    )
                }
            }
    }

    fun insertDeeplink(deepLink: DeepLink) {
        database.transaction {
            database.deepLinkLauncherDatabaseQueries.insertDeeplink(
                id = deepLink.id,
                link = deepLink.link,
                name = deepLink.name,
                description = deepLink.description,
                createdAt = deepLink.createdAt,
                updatedAt = deepLink.updatedAt
            )

            deepLink.folder?.let {
                database.deepLinkLauncherDatabaseQueries.insertFolder(
                    id = deepLink.folder.id,
                    name = deepLink.folder.name,
                    description = deepLink.folder.description,
                    color = deepLink.folder.color
                )
            }

        }
    }
}