package dev.koga.deeplinklauncher.module

import cafe.adriel.voyager.core.registry.screenModule
import dev.koga.deeplinklauncher.FolderDetailsScreen
import dev.koga.navigation.SharedScreen


val folderDetailsModule = screenModule {
    register<SharedScreen.FolderDetails> { provider -> FolderDetailsScreen(provider.id) }
}