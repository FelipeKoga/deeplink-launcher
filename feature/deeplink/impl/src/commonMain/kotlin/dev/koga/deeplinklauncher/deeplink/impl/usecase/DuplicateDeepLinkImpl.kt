@file:OptIn(ExperimentalUuidApi::class, ExperimentalUuidApi::class)

package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.date.currentLocalDateTime
import dev.koga.deeplinklauncher.deeplink.api.model.isLinkValid
import dev.koga.deeplinklauncher.deeplink.api.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.deeplink.api.usecase.DuplicateDeepLink
import kotlinx.coroutines.flow.first
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DuplicateDeepLinkImpl(
    private val repository: DeepLinkRepository,
) : DuplicateDeepLink {

    override suspend operator fun invoke(
        deepLinkId: String,
        newLink: String,
        copyAllFields: Boolean,
    ): DuplicateDeepLink.Result {
        val deepLink = repository.getDeepLinkByIdStream(deepLinkId).first()!!

        if (deepLink.link == newLink) {
            return DuplicateDeepLink.Result.Error.SameLink
        }

        if (!newLink.isLinkValid) {
            return DuplicateDeepLink.Result.Error.InvalidLink
        }

        if (repository.getDeepLinkByLink(newLink) != null) {
            return DuplicateDeepLink.Result.Error.LinkAlreadyExists
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

        return DuplicateDeepLink.Result.Success(duplicatedDeepLink)
    }
}
