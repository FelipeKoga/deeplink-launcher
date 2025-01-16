package dev.koga.deeplinklauncher.devicebridge.di

import dev.koga.deeplinklauncher.DeeplinkTargetStateManager
import dev.koga.deeplinklauncher.devicebridge.DeviceBridge
import dev.koga.deeplinklauncher.devicebridge.DeviceBridgeWrapper
import dev.koga.deeplinklauncher.devicebridge.adb.Adb
import dev.koga.deeplinklauncher.devicebridge.xcrun.Xcrun
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.bind
import org.koin.dsl.module

val deviceBridgeModule = module {
    single { DeeplinkTargetStateManager(get(), Dispatchers.Main) }
    single { Adb.build(dispatcher = Dispatchers.IO) } bind Adb::class
    single { Xcrun.build(dispatcher = Dispatchers.IO) } bind Xcrun::class
    single { DeviceBridgeWrapper(get(), get()) } bind DeviceBridge::class
}