package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.provider.DeepLinkClipboardProvider
import dev.koga.deeplinklauncher.provider.UUIDProvider
import dev.koga.deeplinklauncher.usecase.CopyToClipboard
import dev.koga.deeplinklauncher.usecase.deeplink.ExportDeepLinks
import dev.koga.deeplinklauncher.usecase.deeplink.ImportDeepLinks
import dev.koga.deeplinklauncher.usecase.deeplink.LaunchDeepLink
import dev.koga.deeplinklauncher.usecase.deeplink.ShareDeepLink
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal actual val platformDomainModule: Module = module {
    singleOf(::CopyToClipboard)
    singleOf(::ExportDeepLinks)
    singleOf(::ImportDeepLinks)
    singleOf(::LaunchDeepLink)
    singleOf(::ShareDeepLink)

    single { UUIDProvider }
    singleOf(::DeepLinkClipboardProvider)
}
