package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.model.DeepLink

expect class ShareDeepLink {
    operator fun invoke(deepLink: DeepLink)
}