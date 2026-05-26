package dev.koga.deeplinklauncher.settings.impl.ui.suggestions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import dev.koga.deeplinklauncher.designsystem.DLLSwitch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.koga.deeplinklauncher.designsystem.DLLModalBottomSheet
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionsOptionBottomSheet(
    viewModel: SuggestionsOptionViewModel,
    onDismissRequest: () -> Unit,
) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()
    val typography = DeepLinkTheme.typography

    DLLModalBottomSheet(onDismiss = onDismissRequest) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Suggestions",
                style = typography.title.sheet,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "When you are typing a deeplink, suggestions will be shown below the input" +
                    " based on the deeplinks you already launched.",
                style = typography.body.default,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Enable suggestions",
                    style = typography.label.fieldHeader,
                    modifier = Modifier.weight(1f),
                )

                Spacer(modifier = Modifier.height(24.dp))

                DLLSwitch(
                    checked = enabled,
                    onCheckedChange = viewModel::update,
                )
            }
        }
    }
}
