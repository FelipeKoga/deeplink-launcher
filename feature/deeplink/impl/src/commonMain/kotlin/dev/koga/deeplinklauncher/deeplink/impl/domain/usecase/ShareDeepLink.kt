package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink

internal interface ShareDeepLink {
    operator fun invoke(deepLink: DeepLink)
}
