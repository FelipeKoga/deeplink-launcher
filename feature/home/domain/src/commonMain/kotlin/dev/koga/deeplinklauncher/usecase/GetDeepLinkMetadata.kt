package dev.koga.deeplinklauncher.usecase

import dev.koga.deeplinklauncher.model.DeepLink

expect class GetDeepLinkMetadata {

    fun execute(deepLink: DeepLink): DeepLinkMetadata

}

data class DeepLinkMetadata(
    val deepLink: DeepLink,
    val scheme: String?,
    val host: String?,
    val query: String?,
)