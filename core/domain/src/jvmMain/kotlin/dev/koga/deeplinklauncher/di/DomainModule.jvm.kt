package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.deeplink.LaunchDeepLink
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal actual val platformDomainModule: Module = module {
    singleOf(::LaunchDeepLink)
}
