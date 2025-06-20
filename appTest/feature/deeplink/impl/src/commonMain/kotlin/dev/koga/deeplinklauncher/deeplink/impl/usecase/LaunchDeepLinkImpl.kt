package dev.koga.deeplinklauncher.deeplink.impl.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink.Result

expect class LaunchDeepLinkImpl : LaunchDeepLink {
    override suspend fun launch(url: String): Result
    override suspend fun launch(deepLink: DeepLink): Result
}
