package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails

import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider

internal actual fun detailsDialogProperties(): DialogProperties = DialogProperties(
    dismissOnBackPress = false,
    dismissOnClickOutside = false,
    usePlatformDefaultWidth = false,
    decorFitsSystemWindows = false,
)

@Composable
internal actual fun ConfigureSheetHostWindow() {
    val view = LocalView.current
    DisposableEffect(view) {
        (view.parent as? DialogWindowProvider)?.window?.apply {
            setWindowAnimations(0)
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        onDispose { }
    }
}
