package dev.koga.deeplinklauncher.coroutines.di

import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.coroutines.CoroutineDebouncer
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coroutinesModule = module {
    singleOf(::AppCoroutineScope)
    singleOf(::CoroutineDebouncer)
}
