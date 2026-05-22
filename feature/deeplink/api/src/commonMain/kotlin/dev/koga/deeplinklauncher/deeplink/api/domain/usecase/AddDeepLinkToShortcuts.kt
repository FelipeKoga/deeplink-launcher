package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink

public interface AddDeepLinkToShortcuts {
    public operator fun invoke(deepLink: DeepLink): Result

    public sealed interface Result {
        public data object Added : Result
        public data object NotSupported : Result
    }
}
