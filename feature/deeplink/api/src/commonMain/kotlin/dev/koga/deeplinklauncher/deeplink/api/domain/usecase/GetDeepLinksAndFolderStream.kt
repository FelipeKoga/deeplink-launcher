package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import kotlinx.coroutines.flow.Flow

public interface GetDeepLinksAndFolderStream {
    public operator fun invoke(query: String): Flow<Result>

    public data class Result(
        val deepLinks: List<DeepLink>,
        val favorites: List<DeepLink>,
        val folders: List<Folder>,
        val folderPreviewDeepLinks: List<DeepLink>,
    )
}
