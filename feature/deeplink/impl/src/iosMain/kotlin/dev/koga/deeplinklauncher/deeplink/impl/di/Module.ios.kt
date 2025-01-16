package dev.koga.deeplinklauncher.deeplink.impl.di

import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.impl.usecase.GetIOSDeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.impl.usecase.LaunchIOSDeepLink
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    singleOf(::GetIOSDeepLinkMetadata) bind GetDeepLinkMetadata::class
    singleOf(::LaunchIOSDeepLink) bind LaunchDeepLink::class
}
