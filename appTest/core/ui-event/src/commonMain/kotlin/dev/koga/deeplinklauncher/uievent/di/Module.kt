package dev.koga.deeplinklauncher.uievent.di

import dev.koga.deeplinklauncher.uievent.SnackBarDispatcher
import dev.koga.deeplinklauncher.uievent.SnackBarDispatcherImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val uiEventModule = module {
    singleOf(::SnackBarDispatcherImpl) bind SnackBarDispatcher::class
}
