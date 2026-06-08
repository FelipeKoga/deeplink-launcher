package dev.koga.deeplinklauncher.designsystem.button

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

enum class DLLButtonVariant {
    Primary,
    Secondary,
    Destructive,
}

@Composable
fun DLLButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: DLLButtonVariant = DLLButtonVariant.Primary,
    colors: ButtonColors = dllButtonColors(variant),
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        content = content,
    )
}

@Composable
fun DLLButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: DLLButtonVariant = DLLButtonVariant.Primary,
    colors: ButtonColors = dllButtonColors(variant),
) {
    val typography = DeepLinkTheme.typography

    DLLButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        variant = variant,
        colors = colors,
    ) {
        Text(
            text = text,
            style = typography.action.button,
        )
    }
}

@Composable
fun dllButtonColors(variant: DLLButtonVariant): ButtonColors {
    val colors = DeepLinkTheme.colors

    return when (variant) {
        DLLButtonVariant.Primary -> ButtonDefaults.buttonColors(
            containerColor = colors.button.primaryBackground,
            contentColor = colors.button.primaryContent,
        )

        DLLButtonVariant.Secondary -> ButtonDefaults.filledTonalButtonColors(
            containerColor = colors.button.secondaryBackground,
            contentColor = colors.button.secondaryContent,
        )

        DLLButtonVariant.Destructive -> ButtonDefaults.buttonColors(
            containerColor = colors.button.destructiveBackground,
            contentColor = colors.button.destructiveContent,
        )
    }
}
