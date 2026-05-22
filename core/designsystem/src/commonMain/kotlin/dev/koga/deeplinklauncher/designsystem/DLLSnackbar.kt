package dev.koga.deeplinklauncher.designsystem

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DLLSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val colors = DeepLinkTheme.colors

    SnackbarHost(
        modifier = modifier,
        hostState = hostState,
        snackbar = { data ->
            Snackbar(
                snackbarData = data,
                containerColor = colors.button.primaryBackground.copy(alpha = .95f),
                contentColor = colors.button.primaryContent,
            )
        },
    )
}
