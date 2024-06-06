package dev.koga.deeplinklauncher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator

fun BottomSheetNavigator.navigateToDeepLinkDetails(id: String, showFolder: Boolean = true) {
    val screen = ScreenRegistry.get(SharedScreen.DeepLinkDetails(id, showFolder))
    show(screen)
}


private val results = mutableStateMapOf<String, Any?>()


fun Navigator.popWithResult(key: String, result: Any?) {
    results[key] = result
    pop()
}

fun Navigator.popUntilWithResult(predicate: (Screen) -> Boolean, result: Any?) {
    val currentScreen = lastItem
    results[currentScreen.key] = result
    popUntil(predicate)
}

fun Navigator.clearResults() {
    results.clear()
}

@Composable
fun <T> Navigator.getResult(screenKey: String): State<T?> {
    val result = results[screenKey] as? T
    val resultState = remember(screenKey, result) {
        derivedStateOf {
            results.remove(screenKey)
            result
        }
    }
    return resultState
}

fun BottomSheetNavigator.hideWithResult(result: Any?, key: String) {
    results[key] = result
    this.hide()

}

@Composable
fun <T> BottomSheetNavigator.getResult(screenKey: String): State<T?> {
    val result = results[screenKey] as? T
    val resultState = remember(screenKey, result) {
        derivedStateOf {
            results -= screenKey
            result
        }
    }
    return resultState
}