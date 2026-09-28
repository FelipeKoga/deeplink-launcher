package dev.koga.deeplinklauncher.domain.deeplink.impl.di

import dev.koga.deeplinklauncher.domain.deeplink.api.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkHandlerIcon
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkHandlerInfo
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkHandlers
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.GetDeepLinkMetadata
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.PinDeepLinkToHomeScreen
import dev.koga.deeplinklauncher.domain.deeplink.api.usecase.ValidateDeepLink
import dev.koga.deeplinklauncher.domain.deeplink.impl.manager.DeepLinkShortcutManagerImpl
import dev.koga.deeplinklauncher.domain.deeplink.impl.platform.ClipboardTextReader
import dev.koga.deeplinklauncher.domain.deeplink.impl.platform.android.AndroidHandlerResolver
import dev.koga.deeplinklauncher.domain.deeplink.impl.usecase.GetDeepLinkHandlerIconImpl
import dev.koga.deeplinklauncher.domain.deeplink.impl.usecase.GetDeepLinkHandlerInfoImpl
import dev.koga.deeplinklauncher.domain.deeplink.impl.usecase.GetDeepLinkHandlersImpl
import dev.koga.deeplinklauncher.domain.deeplink.impl.usecase.GetDeepLinkMetadataImpl
import dev.koga.deeplinklauncher.domain.deeplink.impl.usecase.LaunchDeepLinkImpl
import dev.koga.deeplinklauncher.domain.deeplink.impl.usecase.PinDeepLinkToHomeScreenImpl
import dev.koga.deeplinklauncher.domain.deeplink.impl.usecase.ValidateDeepLinkImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    singleOf(::AndroidHandlerResolver)
    singleOf(::GetDeepLinkMetadataImpl) bind GetDeepLinkMetadata::class
    singleOf(::GetDeepLinkHandlerInfoImpl) bind GetDeepLinkHandlerInfo::class
    singleOf(::GetDeepLinkHandlerIconImpl) bind GetDeepLinkHandlerIcon::class
    singleOf(::GetDeepLinkHandlersImpl) bind GetDeepLinkHandlers::class
    singleOf(::LaunchDeepLinkImpl) bind LaunchDeepLink::class
    singleOf(::ValidateDeepLinkImpl) bind ValidateDeepLink::class
    singleOf(::PinDeepLinkToHomeScreenImpl) bind PinDeepLinkToHomeScreen::class
    singleOf(::DeepLinkShortcutManagerImpl) bind DeepLinkShortcutManager::class
    singleOf(::ClipboardTextReader)
}
