package dev.koga.deeplinklauncher.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dev.koga.deeplinklauncher.DeeplinkClipboardManager
import dev.koga.deeplinklauncher.android.core.designsystem.theme.AppTheme
import dev.koga.deeplinklauncher.android.deeplink.home.HomeScreen
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    private lateinit var deepLinkClipboardManager: DeeplinkClipboardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            deepLinkClipboardManager = koinInject<DeeplinkClipboardManager>()

            AppTheme {
                Navigator(HomeScreen) { navigator ->
                    SlideTransition(navigator)
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        deepLinkClipboardManager.initWithCurrentClipboardText()
    }
}