package dev.koga.deeplinklauncher.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.koga.deeplinklauncher.DLLTopBar
import dev.koga.resources.MR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSettingsScreen: () -> Unit,
) {
    DLLTopBar(
        scrollBehavior = scrollBehavior,
        title = "Deeplink Launcher",
        actions = {
            FilledTonalIconButton(
                onClick = onSettingsScreen,
            ) {
                Icon(
                    painter = painterResource(MR.images.ic_settings_24dp),
                    contentDescription = "settings",
                    modifier = Modifier.size(18.dp),
                )
            }
        },
    )
}