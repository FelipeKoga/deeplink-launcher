package dev.koga.deeplinklauncher.home.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.designsystem.DLLTopBarDefaults

@Composable
actual fun HomeTopBarTitle(modifier: Modifier) {
    DLLTopBarDefaults.title(modifier = modifier, text = "DeepLink Launcher")
}
