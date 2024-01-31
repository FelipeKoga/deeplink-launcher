package dev.koga.deeplinklauncher.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import dev.koga.deeplinklauncher.MainApp
import dev.koga.deeplinklauncher.provider.DeepLinkClipboardProvider
import org.koin.compose.koinInject

class MainActivity : FragmentActivity() {

    private var deepLinkClipboardProvider: DeepLinkClipboardProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            deepLinkClipboardProvider = koinInject<DeepLinkClipboardProvider>()

            MainApp()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        deepLinkClipboardProvider?.initWithCurrentClipboardText()
    }
}
