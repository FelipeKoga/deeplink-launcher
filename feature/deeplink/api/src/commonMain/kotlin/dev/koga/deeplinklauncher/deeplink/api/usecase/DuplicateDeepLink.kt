package dev.koga.deeplinklauncher.deeplink.api.usecase

import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink

interface DuplicateDeepLink {
    suspend operator fun invoke(
        deepLinkId: String,
        newLink: String,
        copyAllFields: Boolean,
    ): Result

    sealed interface Result {
        data class Success(val deepLink: DeepLink) : Result
        sealed interface Error : Result {
            data object SameLink : Error
            data object InvalidLink : Error
            data object LinkAlreadyExists : Error
        }
    }
}