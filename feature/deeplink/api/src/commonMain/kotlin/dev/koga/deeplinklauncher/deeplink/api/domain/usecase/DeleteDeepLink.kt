package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

public interface DeleteDeepLink {
    public suspend operator fun invoke(id: String)
}
