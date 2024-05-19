package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.GetDeepLinkMetadata
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformHomeDomainModule: Module = module {
    singleOf(::GetDeepLinkMetadata)
}
