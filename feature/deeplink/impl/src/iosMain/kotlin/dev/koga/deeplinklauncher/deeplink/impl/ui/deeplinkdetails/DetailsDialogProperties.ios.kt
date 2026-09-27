package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties

internal actual fun detailsDialogProperties(): DialogProperties = DialogProperties(
    dismissOnBackPress = true,
    dismissOnClickOutside = false,
    usePlatformDefaultWidth = false,
    usePlatformInsets = false,
    scrimColor = Color.Transparent,
)

@Composable
internal actual fun ConfigureSheetHostWindow() = Unit
