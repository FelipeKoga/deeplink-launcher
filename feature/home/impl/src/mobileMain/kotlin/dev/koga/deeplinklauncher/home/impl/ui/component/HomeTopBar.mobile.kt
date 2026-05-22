package dev.koga.deeplinklauncher.home.impl.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.designsystem.DLLTopBarDefaults

@Composable
actual fun HomeTopBarTitle(modifier: Modifier) {
    DLLTopBarDefaults.Title(modifier = modifier, text = "DeepLink Launcher")
}
