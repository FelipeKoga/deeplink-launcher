import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.koga.deeplinklauncher.MainApp
import dev.koga.deeplinklauncher.initKoin

fun main() = application {
    initKoin()

    val windowState = rememberWindowState()

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "DeepLink Launcher",
    ) {
        MainApp()
    }
}
