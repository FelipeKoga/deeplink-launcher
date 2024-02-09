package dev.koga.deeplinklauncher

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator

fun Navigator.navigateToDeepLinkDetails(id: String) {
    val screen = ScreenRegistry.get(SharedScreen.DeepLinkDetails(id))
    push(screen)
}