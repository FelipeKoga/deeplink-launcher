package dev.koga.deeplinklauncher.deeplink.impl.di

import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkTargetStateManager
import dev.koga.deeplinklauncher.deeplink.impl.domain.manager.DeepLinkTargetStateManagerImpl
import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
@BindingContainer
public object DeepLinkTargetBindings {
    @Provides
    @SingleIn(AppScope::class)
    public fun deepLinkTargetStateManager(deviceBridge: DeviceBridge): DeepLinkTargetStateManager =
        DeepLinkTargetStateManagerImpl(deviceBridge = deviceBridge, dispatcher = Dispatchers.IO)
}
