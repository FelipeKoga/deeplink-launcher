package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink

public interface DuplicateDeepLink {
    public suspend operator fun invoke(
        deepLinkId: String,
        newLink: String,
        copyAllFields: Boolean,
    ): Result

    public sealed interface Result {
        public data class Success(val deepLink: DeepLink) : Result
        public sealed interface Error : Result {
            public data object SameLink : Error
            public data object InvalidLink : Error
            public data object LinkAlreadyExists : Error
        }
    }
}
