package dev.koga.deeplinklauncher.android.di

import dev.koga.deeplinklauncher.android.deeplink.detail.DeepLinkDetailScreenModel
import dev.koga.deeplinklauncher.android.deeplink.home.HomeScreenModel
import dev.koga.deeplinklauncher.android.folder.detail.FolderDetailsScreenModel
import org.koin.dsl.module

val screenModule = module {
    factory { HomeScreenModel(get(), get(), get(), get()) }
    factory { DeepLinkDetailScreenModel(get(), get(), get(), get()) }
    factory { FolderDetailsScreenModel(get(), get()) }
}