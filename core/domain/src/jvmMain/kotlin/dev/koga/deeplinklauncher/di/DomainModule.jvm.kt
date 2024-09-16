package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.DeeplinkTargetStateManager
import dev.koga.deeplinklauncher.devicebridge.DeviceBridge
import dev.koga.deeplinklauncher.devicebridge.DeviceBridgeWrapper
import dev.koga.deeplinklauncher.devicebridge.adb.Adb
import dev.koga.deeplinklauncher.devicebridge.xcrun.Xcrun
import dev.koga.deeplinklauncher.platform.GetFileContent
import dev.koga.deeplinklauncher.platform.PlatformInfo
import dev.koga.deeplinklauncher.platform.SaveFile
import dev.koga.deeplinklauncher.platform.ShareFile
import dev.koga.deeplinklauncher.provider.UUIDProvider
import dev.koga.deeplinklauncher.usecase.GetDeepLinkMetadata
import dev.koga.deeplinklauncher.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.usecase.ShareDeepLink
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformDomainModule: Module = module {
    singleOf(::LaunchDeepLink)
    singleOf(::ShareDeepLink)
    singleOf(::SaveFile)
    singleOf(::ShareFile)
    singleOf(::GetFileContent)
    singleOf(::GetDeepLinkMetadata)
    singleOf(::PlatformInfo)
    single { DeeplinkTargetStateManager(get(), Dispatchers.Main) }
    single { Adb.build(dispatcher = Dispatchers.IO) } bind Adb::class
    single { Xcrun.build(dispatcher = Dispatchers.IO) } bind Xcrun::class
    single { DeviceBridgeWrapper(get(), get()) } bind DeviceBridge::class
    single { UUIDProvider }
}
