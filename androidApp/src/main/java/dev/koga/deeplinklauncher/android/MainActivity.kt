package dev.koga.deeplinklauncher.android

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import dev.koga.deeplinklauncher.MainApp

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.WHITE, Color.BLACK
            ),
            navigationBarStyle = SystemBarStyle.auto(
                Color.WHITE, Color.BLACK
            )
        )


        super.onCreate(savedInstanceState)

        setContent {
            MainApp()
        }
    }
}
