package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.provider.UUIDProvider
import dev.koga.deeplinklauncher.usecase.GetFileContent
import dev.koga.deeplinklauncher.usecase.SaveFile
import dev.koga.deeplinklauncher.usecase.ShareFile
import dev.koga.deeplinklauncher.usecase.deeplink.LaunchDeepLink
import dev.koga.deeplinklauncher.usecase.deeplink.ShareDeepLink
import dev.koga.deeplinklauncher.usecase.deeplink.ValidateDeepLink
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal actual val platformDomainModule: Module = module {
    singleOf(::LaunchDeepLink)
    singleOf(::ShareDeepLink)
    singleOf(::ValidateDeepLink)
    singleOf(::SaveFile)
    singleOf(::ShareFile)
    singleOf(::GetFileContent)

    single { UUIDProvider }
}
