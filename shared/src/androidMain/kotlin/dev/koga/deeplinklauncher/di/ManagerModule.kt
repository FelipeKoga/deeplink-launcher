package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.DeeplinkClipboardManager
import org.koin.dsl.module

val managerModule = module {
    single { DeeplinkClipboardManager(get()) }
}
