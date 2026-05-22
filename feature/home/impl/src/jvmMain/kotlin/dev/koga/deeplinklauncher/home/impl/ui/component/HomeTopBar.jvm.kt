package dev.koga.deeplinklauncher.home.impl.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.home.impl.ui.component.targets.DeepLinkTargetsDropDown

@Composable
actual fun HomeTopBarTitle(modifier: Modifier) {
    DeepLinkTargetsDropDown(modifier = modifier)
}
