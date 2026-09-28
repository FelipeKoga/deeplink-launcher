package dev.koga.deeplinklauncher.domain.deeplink.api.usecase
public interface DeleteDeepLink {
    public suspend operator fun invoke(id: String)
}
