package dev.koga.deeplinklauncher

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator

fun BottomSheetNavigator.navigateToDeepLinkDetails(id: String) {
    val screen = ScreenRegistry.get(SharedScreen.DeepLinkDetails(id))
    show(screen)
}
