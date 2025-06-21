package dev.koga.deeplinklauncher.deeplink.api.usecase

public interface ValidateDeepLink {
    public fun isValid(link: String): Boolean
}
