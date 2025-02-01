package dev.koga.deeplinklauncher.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.koga.deeplinklauncher.preferences.api.model.AppTheme
import dev.koga.deeplinklauncher.preferences.api.repository.PreferencesRepository
import dev.koga.deeplinklauncher.shared.App
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : FragmentActivity(), KoinComponent {

    private val preferencesRepository: PreferencesRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                preferencesRepository.preferencesStream.collectLatest {
                    enableEdgeToEdgeForTheme(it.appTheme)
                }
            }
        }

        setContent {
            App()
        }
    }
}

private fun ComponentActivity.enableEdgeToEdgeForTheme(theme: AppTheme) {
    val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

    val darkColor = ContextCompat.getColor(this, R.color.black700)
    val lightColor = ContextCompat.getColor(this, R.color.white)

    val style = when (theme) {
        AppTheme.LIGHT -> SystemBarStyle.light(lightColor, darkColor)
        AppTheme.DARK -> SystemBarStyle.dark(darkColor)
        // workaround because i don't want translucent system bar
        AppTheme.AUTO -> if (mode == Configuration.UI_MODE_NIGHT_YES) {
            SystemBarStyle.dark(darkColor)
        } else {
            SystemBarStyle.light(lightColor, darkColor)
        }
    }

    enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
}
