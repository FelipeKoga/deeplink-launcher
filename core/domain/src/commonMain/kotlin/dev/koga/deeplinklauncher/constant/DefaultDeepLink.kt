package dev.koga.deeplinklauncher.constant

import dev.koga.deeplinklauncher.model.DeepLink
import kotlinx.datetime.Clock

val defaultDeepLink = DeepLink(
    id = "default-deeplink",
    link = "https://github.com/FelipeKoga/deeplink-launcher",
    name = "Thanks for trying out DeepLink Launcher!",
    description = "If you like this app, leave a star on my GitHub repository and on the PlayStore!",
    createdAt = Clock.System.now(),
    isFavorite = false,
    folder = null,
)