package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkHandlerInfo
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.model.Folder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal sealed interface DeepLinkDetailsUiState {
    val deepLink: DeepLink

    data class Launch(
        override val deepLink: DeepLink,
        val iconPng: ByteArray? = null,
        val showFolder: Boolean = true,
        val folders: ImmutableList<Folder> = persistentListOf(),
        val metadata: DeepLinkMetadata = DeepLinkMetadata(
            link = deepLink.link,
            scheme = null,
            host = null,
            path = null,
            query = null,
        ),
        val handlerInfo: DeepLinkHandlerInfo = DeepLinkHandlerInfo(
            canResolve = false,
            appName = null,
        ),
    ) : DeepLinkDetailsUiState

    data class Edit(
        override val deepLink: DeepLink,
        val folders: ImmutableList<Folder>,
        val errorMessage: String? = null,
    ) : DeepLinkDetailsUiState

    data class Duplicate(
        override val deepLink: DeepLink,
        val errorMessage: String? = null,
    ) : DeepLinkDetailsUiState
}
