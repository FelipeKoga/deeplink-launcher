package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.file.BrowseFileAndGetPath
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformImportModule: Module = module {
    single { BrowseFileAndGetPath(get()) }
}
