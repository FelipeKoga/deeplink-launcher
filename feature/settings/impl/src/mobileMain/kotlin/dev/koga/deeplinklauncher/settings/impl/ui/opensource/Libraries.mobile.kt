package dev.koga.deeplinklauncher.settings.impl.ui.opensource

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries
import dev.koga.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun rememberLibraries(): State<Libs?> {
    return produceLibraries {
        Res.readBytes("files/mobile.aboutlibraries.json").decodeToString()
    }
}
