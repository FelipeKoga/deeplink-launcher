package dev.koga.deeplinklauncher.deeplink.api.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink

public interface LaunchDeepLink {
    public suspend fun launch(url: String): Result
    public suspend fun launch(deepLink: DeepLink): Result

    public sealed interface Result {
        public data class Success(val value: String) : Result
        public data class Failure(val throwable: Throwable) : Result
    }
}
