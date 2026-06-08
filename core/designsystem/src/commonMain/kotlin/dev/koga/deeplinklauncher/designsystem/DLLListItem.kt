package dev.koga.deeplinklauncher.designsystem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DLLListItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val dimensions = DeepLinkTheme.dimensions

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = dimensions.large, horizontal = dimensions.mediumLarge),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = typography.body.emphasis.copy(color = colors.text.primary),
            )

            Text(
                text = description,
                style = typography.body.small.copy(color = colors.text.muted),
            )
        }

        Spacer(modifier = Modifier.width(dimensions.medium))

        trailingContent()
    }
}
