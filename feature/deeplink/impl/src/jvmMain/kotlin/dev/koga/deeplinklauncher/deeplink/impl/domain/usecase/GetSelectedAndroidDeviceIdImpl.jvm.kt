package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkTargetStateManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkTarget
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.GetSelectedAndroidDeviceId
import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge

internal class GetSelectedAndroidDeviceIdImpl(
    private val deepLinkTargetStateManager: DeepLinkTargetStateManager,
) : GetSelectedAndroidDeviceId {
    override fun invoke(): String? {
        val target = deepLinkTargetStateManager.current.value
        return if (target is DeepLinkTarget.Device && target.platform == DeviceBridge.Platform.ANDROID) {
            target.id
        } else {
            null
        }
    }
}
