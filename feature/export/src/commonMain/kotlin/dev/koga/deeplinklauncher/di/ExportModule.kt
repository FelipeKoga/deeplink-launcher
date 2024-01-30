package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.ExportScreenModel
import org.koin.dsl.module

val exportModule = module {
    factory {
        ExportScreenModel(
            getDeepLinksPlainTextPreview = get(),
            getDeepLinksJsonPreview = get(),
        )
    }
}
