package dev.koga.deeplinklauncher.deeplink.ui.di

import dev.koga.deeplinklauncher.deeplink.ui.EnrichDeepLinksForList
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

public val deepLinkUiModule: Module = module {
    singleOf(::EnrichDeepLinksForList)
}
