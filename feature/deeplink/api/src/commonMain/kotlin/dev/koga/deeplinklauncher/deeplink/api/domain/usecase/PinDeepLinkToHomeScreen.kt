package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink

public interface PinDeepLinkToHomeScreen {
    public operator fun invoke(deepLink: DeepLink): Result

    public sealed interface Result {
        public data object Requested : Result
        public data object NotSupported : Result
    }
}
