package dev.koga.deeplinklauncher.designsystem.dialog

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DLLDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    BasicAlertDialog(onDismissRequest, modifier = modifier) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(12.dp),
            content = content
        )
    }

}