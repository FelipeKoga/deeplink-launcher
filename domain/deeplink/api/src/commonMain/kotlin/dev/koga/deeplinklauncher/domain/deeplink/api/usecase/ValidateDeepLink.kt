package dev.koga.deeplinklauncher.domain.deeplink.api.usecase
public interface ValidateDeepLink {
    public fun isValid(link: String): Boolean
}
