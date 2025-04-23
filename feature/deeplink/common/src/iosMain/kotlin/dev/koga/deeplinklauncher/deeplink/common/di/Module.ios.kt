package dev.koga.deeplinklauncher.deeplink.common.di

import dev.koga.deeplinklauncher.deeplink.common.usecase.GetDeepLinkFromClipboard
import dev.koga.deeplinklauncher.deeplink.common.usecase.GetDeepLinkMetadataImpl
import dev.koga.deeplinklauncher.deeplink.common.usecase.LaunchDeepLinkImpl
import dev.koga.deeplinklauncher.deeplink.common.usecase.ShareDeepLinkImpl
import dev.koga.deeplinklauncher.deeplink.common.usecase.ValidateDeepLinkImpl
import dev.koga.deeplinklauncher.deeplink.common.usecase.GetDeepLinkFromClipboardImpl
import dev.koga.deeplinklauncher.deeplink.common.usecase.GetDeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.common.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.common.usecase.ShareDeepLink
import dev.koga.deeplinklauncher.deeplink.common.usecase.ValidateDeepLink
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public actual val platformModule: Module = module {
    singleOf(::GetDeepLinkMetadataImpl) bind GetDeepLinkMetadata::class
    singleOf(::LaunchDeepLinkImpl) bind LaunchDeepLink::class
    singleOf(::ValidateDeepLinkImpl) bind ValidateDeepLink::class
    singleOf(::ShareDeepLinkImpl) bind ShareDeepLink::class
    singleOf(::GetDeepLinkFromClipboardImpl) bind GetDeepLinkFromClipboard::class
}
