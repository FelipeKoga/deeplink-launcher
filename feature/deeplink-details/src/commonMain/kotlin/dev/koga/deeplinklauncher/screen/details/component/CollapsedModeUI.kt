package dev.koga.deeplinklauncher.screen.details.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.DLLSmallChip
import dev.koga.deeplinklauncher.screen.details.state.DeepLinkDetailsUiState

@Composable
fun CollapsedModeUI(
    modifier: Modifier,
    showFolder: Boolean,
    uiState: DeepLinkDetailsUiState,
    onFolderClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        if (!uiState.deepLink.name.isNullOrBlank()) {
            Text(
                text = uiState.deepLink.name.orEmpty(),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        }

        if (!uiState.deepLink.description.isNullOrBlank()) {
            Spacer(modifier = Modifier.padding(top = 2.dp))

            Text(
                text = uiState.deepLink.description.orEmpty(),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            )
        }

        Spacer(modifier = Modifier.padding(top = 12.dp))

        Text(
            text = uiState.deepLink.link,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            ),
        )

        if (showFolder && uiState.deepLink.folder != null) {
            Spacer(modifier = Modifier.padding(top = 24.dp))

            DLLSmallChip(
                label = uiState.deepLink.folder!!.name,
                onClick = onFolderClicked,
            )
        }
    }
}
