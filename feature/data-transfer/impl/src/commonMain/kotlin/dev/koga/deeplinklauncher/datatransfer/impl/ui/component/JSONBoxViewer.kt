package dev.koga.deeplinklauncher.datatransfer.impl.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.designsystem.DLLCodeBlock

@Composable
fun JSONBoxViewer(
    text: String,
    modifier: Modifier = Modifier,
) {
    DLLCodeBlock(text = text, modifier = modifier)
}
