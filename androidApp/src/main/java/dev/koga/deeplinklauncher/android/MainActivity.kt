package dev.koga.deeplinklauncher.android

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
import dev.koga.deeplinklauncher.MainApp
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
            MainApp()
        }
    }
}

private fun ComponentActivity.enableEdgeToEdgeForTheme(theme: AppTheme) {
    val style = when (theme) {
        AppTheme.LIGHT -> SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        AppTheme.DARK -> SystemBarStyle.dark(Color.TRANSPARENT)
        AppTheme.AUTO -> SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
    }

    enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
}
