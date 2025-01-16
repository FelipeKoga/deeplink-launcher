package dev.koga.deeplinklauncher.deeplink.api.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import kotlinx.coroutines.flow.Flow

interface GetDeepLinksAndFolderStream {
    operator fun invoke(query: String): Flow<Result>

    data class Result(
        val deepLinks: List<DeepLink>,
        val favorites: List<DeepLink>,
        val folders: List<Folder>,
    )
}
