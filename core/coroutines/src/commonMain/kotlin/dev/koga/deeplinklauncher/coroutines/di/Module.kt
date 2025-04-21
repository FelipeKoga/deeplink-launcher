package dev.koga.deeplinklauncher.coroutines.di

import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coroutinesModule = module {
    singleOf(::AppCoroutineScope)
    factoryOf(::CoroutineScope)
}