package dev.koga.deeplinklauncher.designsystem

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

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
