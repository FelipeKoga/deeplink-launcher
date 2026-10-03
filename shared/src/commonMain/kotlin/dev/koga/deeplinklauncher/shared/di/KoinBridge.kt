package dev.koga.deeplinklauncher.shared.di

import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.coroutines.AppDispatchers
import dev.koga.deeplinklauncher.coroutines.CoroutineDebouncer
import org.koin.core.module.Module
import org.koin.dsl.module

internal interface KoinBridge {
    val appCoroutineScope: AppCoroutineScope
    val appDispatchers: AppDispatchers
    val coroutineDebouncer: CoroutineDebouncer
}

internal fun KoinBridge.koinBridgeModule(): Module = module {
    single { appCoroutineScope }
    single { appDispatchers }
    single { coroutineDebouncer }
}
