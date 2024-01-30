package dev.koga.deeplinklauncher.usecase

expect class ValidateDeepLink {
    operator fun invoke(deepLink: String): Boolean
}
