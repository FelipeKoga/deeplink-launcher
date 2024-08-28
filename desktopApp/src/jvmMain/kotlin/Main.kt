import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.koga.deeplinklauncher.App
import dev.koga.deeplinklauncher.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun main() = application {
    Napier.base(DebugAntilog())
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
        App()
    }
}
