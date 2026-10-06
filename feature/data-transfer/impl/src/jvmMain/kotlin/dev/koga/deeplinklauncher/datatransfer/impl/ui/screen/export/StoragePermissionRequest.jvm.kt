package dev.koga.deeplinklauncher.datatransfer.impl.ui.screen.export

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState

@Composable
internal actual fun rememberStoragePermissionRequest(onResult: (granted: Boolean) -> Unit): () -> Unit {
    val currentOnResult = rememberUpdatedState(onResult)
    return { currentOnResult.value(true) }
}
