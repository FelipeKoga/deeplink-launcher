package dev.koga.deeplinklauncher.designsystem.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.button.DLLButton
import dev.koga.deeplinklauncher.designsystem.button.DLLButtonVariant
import dev.koga.deeplinklauncher.designsystem.button.DLLTextButton
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
fun DLLConfirmationDialog(
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    dismissLabel: String = "Cancel",
    confirmVariant: DLLButtonVariant = DLLButtonVariant.Destructive,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography
    val dimensions = DeepLinkTheme.dimensions

    DLLDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
    ) {
        Column {
            Text(
                text = title,
                style = typography.title.dialog.copy(color = colors.text.primary),
                modifier = Modifier.padding(dimensions.extraLarge),
            )

            DLLHorizontalDivider()

            Text(
                text = message,
                style = typography.body.default.copy(color = colors.text.primary),
                modifier = Modifier.padding(dimensions.extraLarge),
            )

            Spacer(modifier = Modifier.height(dimensions.mediumLarge))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensions.extraLarge),
            ) {
                DLLTextButton(
                    onClick = onDismissRequest,
                    text = dismissLabel,
                    modifier = Modifier.padding(start = dimensions.mediumLarge),
                )

                Spacer(modifier = Modifier.width(dimensions.extraLarge))

                DLLButton(
                    onClick = onConfirm,
                    text = confirmLabel,
                    variant = confirmVariant,
                    modifier = Modifier.padding(end = dimensions.mediumLarge),
                )
            }
        }
    }
}
