import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.koga.deeplinklauncher.shared.App
import dev.koga.deeplinklauncher.shared.initKoin

fun main() = application {
    initKoin()

    val windowState = rememberWindowState(
        placement = WindowPlacement.Floating,
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "DeepLink Launcher",
        icon = painterResource("ic_launcher_round.webp"),
    ) {
        App()
    }
}
