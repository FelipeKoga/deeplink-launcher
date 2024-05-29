package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.file.GetFileRealPath
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformImportModule: Module = module {
    singleOf(::GetFileRealPath)
}
