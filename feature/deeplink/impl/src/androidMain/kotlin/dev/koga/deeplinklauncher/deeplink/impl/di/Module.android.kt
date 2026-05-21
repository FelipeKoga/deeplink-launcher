package dev.koga.deeplinklauncher.deeplink.impl.di

import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.deeplink.api.usecase.GetDeepLinkMetadata
import dev.koga.deeplinklauncher.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.PinDeepLinkToHomeScreen
import dev.koga.deeplinklauncher.deeplink.api.usecase.ShareDeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.ValidateDeepLink
import dev.koga.deeplinklauncher.deeplink.impl.usecase.GetDeepLinkFromClipboard
import dev.koga.deeplinklauncher.deeplink.impl.usecase.GetDeepLinkHandlerIconImpl
import dev.koga.deeplinklauncher.deeplink.impl.usecase.GetDeepLinkMetadataImpl
import dev.koga.deeplinklauncher.deeplink.impl.usecase.LaunchDeepLinkImpl
import dev.koga.deeplinklauncher.deeplink.impl.usecase.PinDeepLinkToHomeScreenImpl
import dev.koga.deeplinklauncher.deeplink.impl.usecase.ShareDeepLinkImpl
import dev.koga.deeplinklauncher.deeplink.impl.usecase.ValidateDeepLinkImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    singleOf(::GetDeepLinkMetadataImpl) bind GetDeepLinkMetadata::class
    singleOf(::GetDeepLinkHandlerIconImpl) bind GetDeepLinkHandlerIcon::class
    singleOf(::LaunchDeepLinkImpl) bind LaunchDeepLink::class
    singleOf(::ValidateDeepLinkImpl) bind ValidateDeepLink::class
    singleOf(::ShareDeepLinkImpl) bind ShareDeepLink::class
    singleOf(::PinDeepLinkToHomeScreenImpl) bind PinDeepLinkToHomeScreen::class
    singleOf(::GetDeepLinkFromClipboard)
}
