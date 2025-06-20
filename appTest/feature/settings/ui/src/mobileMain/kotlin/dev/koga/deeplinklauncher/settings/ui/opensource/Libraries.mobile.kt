package dev.koga.deeplinklauncher.settings.ui.opensource

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.mikepenz.aboutlibraries.Libs
import dev.koga.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun rememberLibraries(): State<Libs?> {
    return com.mikepenz.aboutlibraries.ui.compose.rememberLibraries {
        Res.readBytes("files/mobile.aboutlibraries.json").decodeToString()
    }
}
