package dev.koga.deeplinklauncher.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dev.koga.deeplinklauncher.android.core.designsystem.theme.AppTheme
import dev.koga.deeplinklauncher.android.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Navigator(HomeScreen) { navigator ->
                    SlideTransition(navigator)
                }
            }
        }
    }
}