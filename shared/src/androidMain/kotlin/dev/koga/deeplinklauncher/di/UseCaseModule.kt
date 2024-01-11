package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.LaunchDeepLink
import org.koin.dsl.module

val useCaseModule = module {
    single { LaunchDeepLink(get()) }
}