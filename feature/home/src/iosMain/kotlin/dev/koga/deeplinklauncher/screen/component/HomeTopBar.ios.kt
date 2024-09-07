package dev.koga.deeplinklauncher.screen.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.DLLTopBarDefaults

@Composable
actual fun HomeTopBarTitle(modifier: Modifier) {
    DLLTopBarDefaults.title(modifier = modifier, text = "DeepLink Launcher")
}
