@file:OptIn(ExperimentalMaterial3Api::class)

package dev.koga.deeplinklauncher.android.core.designsystem

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun DLLTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    actions: @Composable() (RowScope.() -> Unit) = {},
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        navigationIcon = {
            if (onBack != null) {

                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "back")
                }
            }
        },
        actions = actions
    )
}