package dev.koga.deeplinklauncher.domain.deeplink.api.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLinkMetadata
public interface GetDeepLinkMetadata {
    public operator fun invoke(link: String): DeepLinkMetadata
}
