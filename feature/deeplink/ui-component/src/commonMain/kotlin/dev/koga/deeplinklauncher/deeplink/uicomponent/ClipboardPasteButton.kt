package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun ClipboardPasteButton(
    onPaste: (String) -> Unit,
    modifier: Modifier = Modifier,
)
