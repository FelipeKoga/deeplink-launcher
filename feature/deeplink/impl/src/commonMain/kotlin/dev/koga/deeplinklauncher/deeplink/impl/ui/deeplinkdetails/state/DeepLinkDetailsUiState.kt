package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.state

import androidx.compose.runtime.Immutable
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkHandler
import dev.koga.deeplinklauncher.deeplink.api.domain.model.Folder
import dev.koga.deeplinklauncher.deeplink.api.ui.model.DeepLinkDetailsModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal sealed interface DeepLinkDetailsUiState {
    val deepLink: DeepLink

    @Immutable
    data class Launch(
        val details: DeepLinkDetailsModel,
        val showFolder: Boolean = true,
        val folders: ImmutableList<Folder> = persistentListOf(),
        val availableHandlers: ImmutableList<DeepLinkHandler> = persistentListOf(),
    ) : DeepLinkDetailsUiState {
        override val deepLink: DeepLink
            get() = details.deepLink
    }

    data class Edit(
        override val deepLink: DeepLink,
        val folders: ImmutableList<Folder>,
        val errorMessage: String? = null,
        val availableHandlers: ImmutableList<DeepLinkHandler> = persistentListOf(),
    ) : DeepLinkDetailsUiState

    data class Duplicate(
        override val deepLink: DeepLink,
        val errorMessage: String? = null,
    ) : DeepLinkDetailsUiState
}
