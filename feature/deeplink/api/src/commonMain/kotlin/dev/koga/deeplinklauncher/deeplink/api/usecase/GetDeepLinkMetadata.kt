package dev.koga.deeplinklauncher.deeplink.api.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkMetadata

public interface GetDeepLinkMetadata {
    public operator fun invoke(link: String): DeepLinkMetadata
}
