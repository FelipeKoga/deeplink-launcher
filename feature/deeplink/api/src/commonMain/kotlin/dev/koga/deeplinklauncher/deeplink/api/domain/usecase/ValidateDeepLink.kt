package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

public interface ValidateDeepLink {
    public fun isValid(link: String): Boolean
}
