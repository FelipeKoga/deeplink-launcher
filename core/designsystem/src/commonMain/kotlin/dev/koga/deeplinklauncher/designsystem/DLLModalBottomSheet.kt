package dev.koga.deeplinklauncher.designsystem

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DLLModalBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    ),
    containerColor: Color = DeepLinkTheme.colors.surface.card,
    tonalElevation: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var hasResumed by remember { mutableStateOf(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) }

    if (!hasResumed) {
        LaunchedEffect(lifecycle) {
            lifecycle.currentStateFlow.first { it.isAtLeast(Lifecycle.State.RESUMED) }
            hasResumed = true
        }
        return
    }

    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        containerColor = containerColor,
        tonalElevation = tonalElevation,
    ) {
        content()
    }
}
