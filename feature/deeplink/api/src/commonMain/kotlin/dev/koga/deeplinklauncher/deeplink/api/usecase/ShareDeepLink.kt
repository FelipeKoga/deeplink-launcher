package dev.koga.deeplinklauncher.deeplink.api.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink

interface ShareDeepLink {
    operator fun invoke(deepLink: DeepLink)
}
