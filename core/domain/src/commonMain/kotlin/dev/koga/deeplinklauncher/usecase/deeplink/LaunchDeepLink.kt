package dev.koga.deeplinklauncher.usecase.deeplink

import dev.koga.deeplinklauncher.model.DeepLink

expect class LaunchDeepLink {
    fun launch(url: String): LaunchDeepLinkResult
    fun launch(deepLink: DeepLink): LaunchDeepLinkResult
}

sealed interface LaunchDeepLinkResult {
    data class Success(val value: String) : LaunchDeepLinkResult
    data class Failure(val throwable: Throwable) : LaunchDeepLinkResult
}
