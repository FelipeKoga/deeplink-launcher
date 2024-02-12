package dev.koga.deeplinklauncher.util.ext

import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.provider.UUIDProvider

fun String.toDeepLink(): DeepLink {
    return DeepLink(
        id = UUIDProvider.get(),
        createdAt = currentLocalDateTime,
        link = this,
        name = null,
        description = null,
        isFavorite = false,
        folder = null,
    )
}
