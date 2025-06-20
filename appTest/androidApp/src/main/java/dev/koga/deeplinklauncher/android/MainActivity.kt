package dev.koga.deeplinklauncher.android

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import dev.koga.deeplinklauncher.shared.App
import org.koin.core.component.KoinComponent

class MainActivity : FragmentActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
            ),
        )
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}
