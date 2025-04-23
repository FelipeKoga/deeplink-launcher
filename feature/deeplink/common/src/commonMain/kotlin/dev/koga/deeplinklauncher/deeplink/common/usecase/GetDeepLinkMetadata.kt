package dev.koga.deeplinklauncher.deeplink.common.usecase

import dev.koga.deeplinklauncher.deeplink.common.model.DeepLinkMetadata

public interface GetDeepLinkMetadata {
    public operator fun invoke(link: String): DeepLinkMetadata
}
