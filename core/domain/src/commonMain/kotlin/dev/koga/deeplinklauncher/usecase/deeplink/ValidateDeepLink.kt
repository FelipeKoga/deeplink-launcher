package dev.koga.deeplinklauncher.usecase.deeplink

expect class ValidateDeepLink {
    operator fun invoke(deepLink: String): Boolean
}