package dev.koga.deeplinklauncher.deeplink.api.usecase

interface ValidateDeepLink {
    fun isValid(link: String): Boolean
}
