package dev.koga.deeplinklauncher.usecase

expect class LaunchDeepLink {
    fun launch(url: String): LaunchDeepLinkResult

}

sealed interface LaunchDeepLinkResult {
    data class Success(val value: String) : LaunchDeepLinkResult
    data class Failure(val throwable: Throwable) : LaunchDeepLinkResult
}