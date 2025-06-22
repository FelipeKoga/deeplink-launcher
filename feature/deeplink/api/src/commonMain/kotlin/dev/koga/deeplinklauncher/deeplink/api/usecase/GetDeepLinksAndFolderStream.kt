package dev.koga.deeplinklauncher.deeplink.api.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import kotlinx.coroutines.flow.Flow

public interface GetDeepLinksAndFolderStream {
    public operator fun invoke(query: String): Flow<Result>

    public data class Result(
        val deepLinks: List<DeepLink>,
        val favorites: List<DeepLink>,
        val folders: List<Folder>,
    )
}
