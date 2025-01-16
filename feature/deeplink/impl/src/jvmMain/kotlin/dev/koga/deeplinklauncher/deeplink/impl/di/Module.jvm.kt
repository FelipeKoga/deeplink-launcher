package dev.koga.deeplinklauncher.deeplink.impl.di

import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.impl.usecase.GetJvmDeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.impl.usecase.LaunchJvmDeepLink
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    singleOf(::GetJvmDeepLinkMetadata) bind GetDeepLinkMetadata::class
    singleOf(::LaunchJvmDeepLink) bind LaunchJvmDeepLink::class
}
