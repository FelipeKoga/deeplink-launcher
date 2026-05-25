package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkAssertion
import dev.koga.deeplinklauncher.designsystem.button.DLLButton
import dev.koga.deeplinklauncher.designsystem.button.DLLButtonVariant
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@Composable
internal actual fun DeepLinkAssertionSection(
    assertion: DeepLinkAssertion?,
    captureMessage: String?,
    onAssertionChanged: (DeepLinkAssertion?) -> Unit,
    onCaptureCurrentState: () -> Unit,
    modifier: Modifier,
) {
    val typography = DeepLinkTheme.typography
    val colors = DeepLinkTheme.colors
    var current by remember(assertion) { mutableStateOf(assertion ?: DeepLinkAssertion()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Test verification",
            style = typography.label.caption,
        )

        OutlinedTextField(
            value = current.expectedPackage.orEmpty(),
            onValueChange = {
                current = current.copy(expectedPackage = it)
                onAssertionChanged(current.takeIf { value -> value.hasCriteria })
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Expected package") },
            singleLine = true,
        )

        OutlinedTextField(
            value = current.expectedActivityPattern.orEmpty(),
            onValueChange = {
                current = current.copy(expectedActivityPattern = it)
                onAssertionChanged(current.takeIf { value -> value.hasCriteria })
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Expected activity (regex)") },
            singleLine = true,
        )

        OutlinedTextField(
            value = current.expectedTexts.joinToString("\n"),
            onValueChange = { raw ->
                current = current.copy(
                    expectedTexts = raw.lines().map { it.trim() }.filter { it.isNotBlank() },
                )
                onAssertionChanged(current.takeIf { value -> value.hasCriteria })
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Expected on-screen text (one per line)") },
            minLines = 2,
        )

        Text(
            text = "Wait after launch: ${(current.waitAfterLaunchMs / 1000f).let { "%.1f".format(it) }}s",
            style = typography.label.caption,
        )

        Slider(
            value = current.waitAfterLaunchMs.coerceIn(
                DeepLinkAssertion.MIN_WAIT_AFTER_LAUNCH_MS,
                DeepLinkAssertion.MAX_WAIT_AFTER_LAUNCH_MS,
            ).toFloat(),
            onValueChange = {
                current = current.copy(waitAfterLaunchMs = it.toLong())
                onAssertionChanged(current.takeIf { value -> value.hasCriteria })
            },
            valueRange = DeepLinkAssertion.MIN_WAIT_AFTER_LAUNCH_MS.toFloat()
                ..DeepLinkAssertion.MAX_WAIT_AFTER_LAUNCH_MS.toFloat(),
            steps = 4,
        )

        DLLButton(
            onClick = onCaptureCurrentState,
            text = "Capture current device state",
            variant = DLLButtonVariant.Secondary,
            modifier = Modifier.fillMaxWidth(),
        )

        captureMessage?.let {
            Text(
                text = it,
                style = typography.label.caption.copy(color = colors.text.muted),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
    }
}
