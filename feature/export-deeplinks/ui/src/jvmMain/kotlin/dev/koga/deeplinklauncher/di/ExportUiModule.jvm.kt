package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.permission.StoragePermission
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformUiModule: Module = module {
    singleOf(::StoragePermission)
}