package dev.koga.deeplinklauncher.android

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import dev.koga.deeplinklauncher.shared.App
import dev.koga.deeplinklauncher.shared.isAppThemeInDarkTheme
import org.koin.core.component.KoinComponent

class MainActivity : FragmentActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        super.onCreate(savedInstanceState)

        val windowInsetsController = WindowCompat.getInsetsController(
            window,
            window.decorView,
        )

        setContent {
            App()

            val isDarkTheme = isAppThemeInDarkTheme()
            LaunchedEffect(isDarkTheme) {
                windowInsetsController.isAppearanceLightStatusBars = !isDarkTheme
                windowInsetsController.isAppearanceLightNavigationBars = !isDarkTheme
            }
        }
    }
}
