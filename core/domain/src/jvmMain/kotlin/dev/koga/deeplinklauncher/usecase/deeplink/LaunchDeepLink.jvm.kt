package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.model.DeepLink

actual class LaunchDeepLink {
    actual fun launch(url: String): LaunchDeepLinkResult {
        return LaunchDeepLinkResult.Failure(Throwable(""))
    }

    actual fun launch(deepLink: DeepLink): LaunchDeepLinkResult {
        TODO("Not yet implemented")
    }
}
