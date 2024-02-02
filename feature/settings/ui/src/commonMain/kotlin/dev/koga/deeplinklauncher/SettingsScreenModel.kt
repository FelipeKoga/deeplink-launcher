package dev.koga.deeplinklauncher

import cafe.adriel.voyager.core.model.ScreenModel
import dev.koga.deeplinklauncher.datasource.DeepLinkDataSource
import dev.koga.deeplinklauncher.datasource.FolderDataSource
import dev.koga.deeplinklauncher.platform.PlatformInfo
import dev.koga.deeplinklauncher.usecase.deeplink.LaunchDeepLink

class SettingsScreenModel(
    private val deepLinkDataSource: DeepLinkDataSource,
    private val folderDataSource: FolderDataSource,
    private val launchDeepLink: LaunchDeepLink,
    private val platformInfo: PlatformInfo,
) : ScreenModel {

    val appVersion = platformInfo.version

    fun deleteAllDeepLinks() {
        deepLinkDataSource.deleteAll()
    }

    fun deleteAllFolders() {
        folderDataSource.deleteAll()
    }

    fun deleteAllData() {
        deepLinkDataSource.deleteAll()
        folderDataSource.deleteAll()
    }

    fun navigateToStore() {
        launchDeepLink.launch(platformInfo.storePath)
    }

    fun navigateToGithub() {
        launchDeepLink.launch("https://github.com/FelipeKoga/deeplink-launcher")
    }
}