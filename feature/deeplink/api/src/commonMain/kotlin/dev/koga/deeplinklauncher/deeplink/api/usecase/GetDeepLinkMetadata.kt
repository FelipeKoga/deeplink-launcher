package dev.koga.deeplinklauncher.deeplink.api.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkMetadata

interface GetDeepLinkMetadata {
    operator fun invoke(link: String): DeepLinkMetadata
}
