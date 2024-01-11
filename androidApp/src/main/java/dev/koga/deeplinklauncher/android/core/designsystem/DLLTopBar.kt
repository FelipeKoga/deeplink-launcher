@file:OptIn(ExperimentalMaterial3Api::class)

package dev.koga.deeplinklauncher.android.core.designsystem

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun DLLTopBar(
    title: String,
    actions: @Composable() (RowScope.() -> Unit),
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        actions = actions
    )
}