package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
internal interface ShareDeepLink {
    operator fun invoke(deepLink: DeepLink)
}
