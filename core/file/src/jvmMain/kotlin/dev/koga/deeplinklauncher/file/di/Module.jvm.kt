package dev.koga.deeplinklauncher.file.di

import dev.koga.deeplinklauncher.file.GetFileContent
import dev.koga.deeplinklauncher.file.GetFileRealPath
import dev.koga.deeplinklauncher.file.SaveFile
import dev.koga.deeplinklauncher.file.ShareFile
import dev.koga.deeplinklauncher.file.StoragePermission
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::GetFileContent)
    singleOf(::GetFileRealPath)
    singleOf(::SaveFile)
    singleOf(::ShareFile)
    singleOf(::StoragePermission)
}