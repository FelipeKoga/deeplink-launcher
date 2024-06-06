package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.isLinkValid
import dev.koga.deeplinklauncher.provider.UUIDProvider
import dev.koga.deeplinklauncher.util.ext.currentLocalDateTime
import kotlinx.coroutines.flow.first

class DuplicateDeepLink(
    private val dataSource: DeepLinkDataSource,
) {

    suspend operator fun invoke(
        deepLinkId: String,
        newLink: String,
        copyAllFields: Boolean,
    ): Response {
        val deepLink = dataSource.getDeepLinkByIdStream(deepLinkId).first()!!

        if (deepLink.link == newLink) {
            return Response.Error.SameLink
        }

        if (!newLink.isLinkValid) {
            return Response.Error.InvalidLink
        }

        if (dataSource.getDeepLinkByLink(newLink) != null) {
            return Response.Error.LinkAlreadyExists
        }

        val id = UUIDProvider.get()
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

        dataSource.upsertDeepLink(duplicatedDeepLink)

        return Response.Success(duplicatedDeepLink)
    }

    sealed interface Response {
        data class Success(val deepLink: DeepLink) : Response
        sealed interface Error : Response {
            data object SameLink : Error
            data object InvalidLink : Error
            data object LinkAlreadyExists : Error
        }
    }
}
