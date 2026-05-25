package dev.koga.deeplinklauncher.deeplink.api.domain.usecase

public interface GetSelectedAndroidDeviceId {
    public operator fun invoke(): String?
}
