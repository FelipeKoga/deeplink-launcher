package dev.koga.deeplinklauncher.screen.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.DLLTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal actual fun HomeTopBarImpl(
    modifier: Modifier,
    navigationIcon: @Composable (() -> Unit)?,
    actions: @Composable (RowScope.() -> Unit),
    scrollBehavior: TopAppBarScrollBehavior?,
) {
    DLLTopBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = "Deeplink Launcher",
        actions = actions
    )
}