package dev.koga.deeplinklauncher.android

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.koga.deeplinklauncher.App
import dev.koga.deeplinklauncher.datasource.PreferencesDataSource
import dev.koga.deeplinklauncher.model.AppTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : FragmentActivity(), KoinComponent {

    private val preferencesDataSource: PreferencesDataSource by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdgeForTheme(AppTheme.AUTO)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                preferencesDataSource.preferencesStream.collectLatest {
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

    val darkColor = Color.parseColor("#09090B")
    val lightColor = Color.parseColor("#FFFFFF")
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
