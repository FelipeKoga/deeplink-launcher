package dev.koga.deeplinklauncher.devicebridge.impl.di

import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge
import dev.koga.deeplinklauncher.devicebridge.impl.CompositeDeviceBridge
import dev.koga.deeplinklauncher.devicebridge.impl.adb.Adb
import dev.koga.deeplinklauncher.devicebridge.impl.xcrun.Xcrun
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val deviceBridgeModule = module {
    single { Adb.build(dispatcher = Dispatchers.IO) } bind Adb::class
    single { Xcrun.build(dispatcher = Dispatchers.IO) } bind Xcrun::class
    singleOf(::CompositeDeviceBridge) bind DeviceBridge::class
}
