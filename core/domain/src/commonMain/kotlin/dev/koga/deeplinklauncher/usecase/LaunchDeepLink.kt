package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.DeepLink

expect class LaunchDeepLink {
    suspend fun launch(url: String): LaunchDeepLinkResult
    suspend fun launch(deepLink: DeepLink): LaunchDeepLinkResult
}

sealed interface LaunchDeepLinkResult {
    data class Success(val value: String) : LaunchDeepLinkResult
    data class Failure(val throwable: Throwable) : LaunchDeepLinkResult
}
