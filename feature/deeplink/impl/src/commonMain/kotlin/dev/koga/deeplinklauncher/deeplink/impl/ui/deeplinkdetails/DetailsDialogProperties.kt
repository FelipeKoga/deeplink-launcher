package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

internal expect fun detailsDialogProperties(): DialogProperties

@Composable
internal expect fun ConfigureSheetHostWindow()
