package dev.koga.deeplinklauncher.shared

import cafe.adriel.voyager.core.registry.screenModule
import dev.koga.deeplinklauncher.SharedScreen
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.DeepLinkDetailsScreen
import dev.koga.deeplinklauncher.deeplink.ui.folder.screen.details.FolderDetailsScreen
import dev.koga.deeplinklauncher.importdata.ui.screen.export.ExportScreen
import dev.koga.deeplinklauncher.importdata.ui.screen.import.ImportScreen
import dev.koga.deeplinklauncher.settings.ui.screen.SettingsScreen
import org.koin.core.module.Module

internal val screenModule = screenModule {
    register<SharedScreen.ImportDeepLinks> { ImportScreen() }
    register<SharedScreen.ExportDeepLinks> { ExportScreen() }
    register<SharedScreen.Settings> { SettingsScreen() }
    register<SharedScreen.DeepLinkDetails> { provider ->
        DeepLinkDetailsScreen(
            provider.id,
            provider.showFolder,
        )
    }
    register<SharedScreen.FolderDetails> { provider -> FolderDetailsScreen(provider.id) }
}

internal expect val platformModule: Module
