package dev.koga.deeplinklauncher.designsystem.dialog

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DLLDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = DeepLinkTheme.colors
    val shapes = DeepLinkTheme.shapes

    BasicAlertDialog(onDismissRequest, modifier = modifier) {
        Card(
            modifier = modifier,
            shape = shapes.dialog,
            colors = CardDefaults.cardColors(
                containerColor = colors.surface.card,
                contentColor = colors.text.primary,
            ),
            content = content,
        )
    }
}
