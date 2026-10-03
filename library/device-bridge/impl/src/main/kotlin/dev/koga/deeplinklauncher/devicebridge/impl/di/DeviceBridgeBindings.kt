package dev.koga.deeplinklauncher.devicebridge.impl.di

import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge
import dev.koga.deeplinklauncher.devicebridge.impl.CompositeDeviceBridge
import dev.koga.deeplinklauncher.devicebridge.impl.adb.Adb
import dev.koga.deeplinklauncher.devicebridge.impl.xcrun.Xcrun
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
@BindingContainer
object DeviceBridgeBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun deviceBridge(): DeviceBridge = CompositeDeviceBridge(
        adb = Adb.build(dispatcher = Dispatchers.IO),
        xcrun = Xcrun.build(dispatcher = Dispatchers.IO),
    )
}
