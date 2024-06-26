import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.koga.deeplinklauncher.MainApp
import dev.koga.deeplinklauncher.initKoin

fun main() = application {
    initKoin()

    val windowState = rememberWindowState(
        placement = WindowPlacement.Maximized,
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "DeepLink Launcher",
        icon = painterResource("ic_launcher_round.webp"),
    ) {
        MainApp()
    }
}
