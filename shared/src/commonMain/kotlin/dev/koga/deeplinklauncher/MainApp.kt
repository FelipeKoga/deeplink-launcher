package dev.koga.deeplinklauncher

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

@Composable
fun MainApp() {
    AppTheme {
        AppTheme {
            Navigator(HomeScreen) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}