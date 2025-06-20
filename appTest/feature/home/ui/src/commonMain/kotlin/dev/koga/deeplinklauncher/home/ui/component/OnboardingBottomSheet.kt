package dev.koga.deeplinklauncher.home.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.DLLModalBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
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
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                ),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Thank you for trying out DeepLink Launcher.",
                style = MaterialTheme.typography.titleMedium.copy(
                    textAlign = TextAlign.Center,

                ),
            )

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = "Please note that data is stored locally on your device. If you wish to keep a backup" +
                    " of your data, you can export it from the settings screen.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                ),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier.padding(top = 12.dp).fillMaxWidth(.5f),
            ) {
                Text(text = "Got it!")
            }
        }
    }
}
