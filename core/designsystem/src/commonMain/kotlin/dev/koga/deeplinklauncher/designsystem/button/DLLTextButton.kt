package dev.koga.deeplinklauncher.designsystem.button

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

enum class DLLTextButtonVariant {
    Default,
    Destructive,
}

@Composable
fun DLLTextButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: DLLTextButtonVariant = DLLTextButtonVariant.Default,
    colors: ButtonColors = dllTextButtonColors(variant),
) {
    val typography = DeepLinkTheme.typography

    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
    ) {
        Text(
            text = text,
            style = typography.action.button,
        )
    }
}

@Composable
fun dllTextButtonColors(variant: DLLTextButtonVariant): ButtonColors {
    val colors = DeepLinkTheme.colors

    return when (variant) {
        DLLTextButtonVariant.Default -> ButtonDefaults.textButtonColors(
            contentColor = colors.button.textContent,
        )

        DLLTextButtonVariant.Destructive -> ButtonDefaults.textButtonColors(
            contentColor = colors.button.textDestructiveContent,
        )
    }
}
