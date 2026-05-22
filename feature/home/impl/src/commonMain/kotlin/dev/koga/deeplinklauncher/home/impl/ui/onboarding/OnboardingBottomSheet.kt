package dev.koga.deeplinklauncher.home.impl.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.DLLModalBottomSheet
import dev.koga.deeplinklauncher.designsystem.button.DLLButton
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography

    DLLModalBottomSheet(
        modifier = modifier,
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Welcome!",
                style = typography.title.sheet.copy(
                    color = colors.text.primary,
                    textAlign = TextAlign.Center,
                ),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Thank you for trying out DeepLink Launcher.",
                style = typography.title.card.copy(
                    color = colors.text.primary,
                    textAlign = TextAlign.Center,
                ),
            )

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = "Please note that data is stored locally on your device. If you wish to keep a backup" +
                    " of your data, you can export it from the settings screen.",
                style = typography.body.emphasis.copy(
                    color = colors.text.primary,
                    textAlign = TextAlign.Center,
                ),
            )

            Spacer(modifier = Modifier.height(12.dp))

            DLLButton(
                onClick = onDismiss,
                text = "Got it!",
                modifier = Modifier.padding(top = 12.dp).fillMaxWidth(.5f),
            )
        }
    }
}
