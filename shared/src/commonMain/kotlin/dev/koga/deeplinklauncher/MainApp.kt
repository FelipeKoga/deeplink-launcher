package dev.koga.deeplinklauncher

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dev.koga.deeplinklauncher.theme.AppTheme

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