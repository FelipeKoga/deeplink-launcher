package dev.koga.deeplinklauncher.deeplink.api.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink

public interface PinDeepLinkToHomeScreen {
    public operator fun invoke(deepLink: DeepLink): Result

    public sealed interface Result {
        public data object Requested : Result
        public data object NotSupported : Result
    }
}
