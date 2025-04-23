@file:OptIn(ExperimentalUuidApi::class, ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.deeplink.common.usecase

import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.common.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.common.repository.DeepLinkRepository
import kotlinx.coroutines.flow.first
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public class DuplicateDeepLink(
    private val repository: DeepLinkRepository,
    private val validateDeepLink: ValidateDeepLink,
) {

    public suspend operator fun invoke(
        deepLinkId: String,
        newLink: String,
        copyAllFields: Boolean,
    ): Output {
        val deepLink = repository.getDeepLinkByIdStream(deepLinkId).first()
            ?: return Output.Error.InvalidLink

        if (deepLink.link == newLink) {
            return Output.Error.SameLink
        }

        if (!validateDeepLink.isValid(newLink)) {
            return Output.Error.InvalidLink
        }

        if (repository.getDeepLinkByLink(newLink) != null) {
            return Output.Error.LinkAlreadyExists
        }

        val id = Uuid.random().toString()

        val duplicatedDeepLink = deepLink.copy(
            id = id,
            link = newLink,
            name = if (copyAllFields) deepLink.name else null,
            description = if (copyAllFields) deepLink.description else null,
            createdAt = currentLocalDateTime,
            lastLaunchedAt = null,
            folder = if (copyAllFields) deepLink.folder else null,
            isFavorite = if (copyAllFields) deepLink.isFavorite else false,
        )

        repository.upsertDeepLink(duplicatedDeepLink)

        return Output.Success(duplicatedDeepLink)
    }

    public sealed interface Output {
        public data class Success(val deepLink: DeepLink) : Output
        public sealed interface Error : Output {
            public data object SameLink : Error
            public data object InvalidLink : Error
            public data object LinkAlreadyExists : Error
        }
    }

}
