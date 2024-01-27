package dev.koga.deeplinklauncher.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.koga.deeplinklauncher.MainApp
import dev.koga.deeplinklauncher.provider.DeepLinkClipboardProvider
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    private lateinit var deepLinkClipboardProvider: DeepLinkClipboardProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            deepLinkClipboardProvider = koinInject<DeepLinkClipboardProvider>()

            MainApp()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        deepLinkClipboardProvider.initWithCurrentClipboardText()
    }
}