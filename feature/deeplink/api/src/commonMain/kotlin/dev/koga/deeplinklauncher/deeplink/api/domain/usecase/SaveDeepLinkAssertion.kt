package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkAssertion

public interface SaveDeepLinkAssertion {
    public operator fun invoke(deepLinkId: String, assertion: DeepLinkAssertion?)
}
