package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.usecase.CopyToClipboard
import dev.koga.deeplinklauncher.usecase.ExportDeepLinks
import dev.koga.deeplinklauncher.usecase.LaunchDeepLink
import dev.koga.deeplinklauncher.usecase.ShareDeepLink
import org.koin.dsl.module

val useCaseModule = module {
    single { LaunchDeepLink(get()) }
    single { ShareDeepLink(get()) }
    single { CopyToClipboard(get()) }
    single { ExportDeepLinks(get(), get()) }
}