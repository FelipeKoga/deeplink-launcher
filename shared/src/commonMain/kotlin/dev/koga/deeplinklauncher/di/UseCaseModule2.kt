package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.GetDeepLinksJsonPreview
import dev.koga.deeplinklauncher.usecase.GetDeepLinksPlainTextPreview
import org.koin.dsl.module

val useCaseModule2 = module {
    single { GetDeepLinksPlainTextPreview(get()) }
    single { GetDeepLinksJsonPreview(get()) }
}