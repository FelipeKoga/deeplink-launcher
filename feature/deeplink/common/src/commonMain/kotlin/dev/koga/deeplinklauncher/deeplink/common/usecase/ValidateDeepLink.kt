package dev.koga.deeplinklauncher.deeplink.common.usecase

public interface ValidateDeepLink {
    public fun isValid(link: String): Boolean
}
