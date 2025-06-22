package dev.koga.deeplinklauncher.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.home.component.targets.DeepLinkTargetsDropDown

@Composable
actual fun HomeTopBarTitle(modifier: Modifier) {
    DeepLinkTargetsDropDown(modifier = modifier)
}
