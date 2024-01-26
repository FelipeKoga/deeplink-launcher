package dev.koga.deeplinklauncher.di

import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSourceImpl
import dev.koga.deeplinklauncher.datasource.FolderDataSource
import dev.koga.deeplinklauncher.datasource.FolderDataSourceImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseSourceModule = module {
    singleOf(::DeepLinkDataSourceImpl) bind DeepLinkDataSource::class
    singleOf(::FolderDataSourceImpl) bind FolderDataSource::class
    includes(platformDatabaseModule)
}

internal expect val platformDatabaseModule: Module
