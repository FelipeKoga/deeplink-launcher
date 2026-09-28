package dev.koga.deeplinklauncher.deeplink.impl.di

import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.ShareDeepLink
import dev.koga.deeplinklauncher.deeplink.impl.domain.usecase.ShareDeepLinkImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    singleOf(::ShareDeepLinkImpl) bind ShareDeepLink::class
}
