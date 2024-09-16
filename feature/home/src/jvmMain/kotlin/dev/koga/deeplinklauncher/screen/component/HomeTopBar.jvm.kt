package dev.koga.deeplinklauncher.screen.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.screen.component.targets.DeeplinkTargetsDropDown

@Composable
actual fun HomeTopBarTitle(modifier: Modifier) {
    DeeplinkTargetsDropDown(modifier = modifier)
}
