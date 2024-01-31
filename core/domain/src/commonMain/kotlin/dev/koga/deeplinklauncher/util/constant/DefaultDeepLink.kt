package dev.koga.deeplinklauncher.util.constant

import dev.koga.deeplinklauncher.model.DeepLink
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val defaultDeepLink = DeepLink(
    id = "default-deeplink",
    link = "https://github.com/FelipeKoga/deeplink-launcher",
    name = "Thanks for trying out DeepLink Launcher!",
    description = "If you like this app, leave a star on my GitHub repository and on the PlayStore!",
    createdAt = Clock.System.now().toLocalDateTime(
        TimeZone.currentSystemDefault(),
    ),
    isFavorite = false,
    folder = null,
)
