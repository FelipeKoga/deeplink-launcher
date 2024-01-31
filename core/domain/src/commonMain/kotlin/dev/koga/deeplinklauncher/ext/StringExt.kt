package dev.koga.deeplinklauncher.ext

import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.provider.UUIDProvider
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.toDeepLink(): DeepLink {
    return DeepLink(
        id = UUIDProvider.get(),
        createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        link = this,
        name = null,
        description = null,
        isFavorite = false,
        folder = null,
    )
}
