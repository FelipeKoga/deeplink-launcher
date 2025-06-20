package dev.koga.deeplinklauncher.deeplink.api.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink

interface LaunchDeepLink {
    suspend fun launch(url: String): Result
    suspend fun launch(deepLink: DeepLink): Result

    sealed interface Result {
        data class Success(val value: String) : Result
        data class Failure(val throwable: Throwable) : Result
    }
}
