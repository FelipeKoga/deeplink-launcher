package dev.koga.deeplinklauncher

import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator

fun BottomSheetNavigator.navigateToDeepLinkDetails(id: String, showFolder: Boolean = true) {
    val screen = ScreenRegistry.get(SharedScreen.DeepLinkDetails(id, showFolder))
    show(screen)
}

val LocalRootNavigator = staticCompositionLocalOf<Navigator> {
    error("No RootNavigator provided")
}
