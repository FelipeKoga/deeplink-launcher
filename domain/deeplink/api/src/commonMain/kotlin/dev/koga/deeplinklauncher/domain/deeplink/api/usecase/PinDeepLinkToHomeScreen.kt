package dev.koga.deeplinklauncher.domain.deeplink.api.usecase
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
public interface PinDeepLinkToHomeScreen {
    public operator fun invoke(deepLink: DeepLink): Result

    public sealed interface Result {
        public data object Requested : Result
        public data object NotSupported : Result
    }
}
