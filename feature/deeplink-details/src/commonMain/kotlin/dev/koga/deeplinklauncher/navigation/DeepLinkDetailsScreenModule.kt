package dev.koga.deeplinklauncher.navigation

import cafe.adriel.voyager.core.registry.screenModule
import dev.koga.deeplinklauncher.DeepLinkDetailsScreen
import dev.koga.navigation.SharedScreen


val deepLinkDetailsScreenModule = screenModule {
    register<SharedScreen.DeepLinkDetails> { provider -> DeepLinkDetailsScreen(provider.id) }
}