package dev.koga.deeplinklauncher.android

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.koga.deeplinklauncher.preferences.model.AppTheme
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import dev.koga.deeplinklauncher.shared.App
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent

class MainActivity : FragmentActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
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

@Composable
private fun isAppThemeInDarkTheme(
    preferencesDataSource: PreferencesDataSource = koinInject(),
): Boolean {
    val isSystemDarkTheme = isSystemInDarkTheme()

    val preferences by preferencesDataSource.preferencesStream.collectAsStateWithLifecycle(preferencesDataSource.preferences)

    return when (preferences.appTheme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.AUTO -> isSystemDarkTheme
    }
}
