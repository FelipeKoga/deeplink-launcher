package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkMetadata

public interface GetDeepLinkMetadata {
    public operator fun invoke(link: String): DeepLinkMetadata
}
