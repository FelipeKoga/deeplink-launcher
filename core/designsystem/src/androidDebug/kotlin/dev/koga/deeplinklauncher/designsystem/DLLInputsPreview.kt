package dev.koga.deeplinklauncher.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.designsystem.theme.DLLPreviewTheme
import kotlinx.collections.immutable.persistentListOf

@PreviewLightDark
@Composable
private fun DLLTextFieldEmptyPreview() {
    DLLPreviewTheme {
        DLLTextField(
            modifier = Modifier.padding(16.dp),
            value = "",
            onValueChange = {},
            label = "Name",
        )
    }
}

@PreviewLightDark
@Composable
private fun DLLTextFieldFilledPreview() {
    DLLPreviewTheme {
        DLLTextField(
            modifier = Modifier.padding(16.dp),
            value = "https://example.com/products/42",
            onValueChange = {},
            label = "Link",
        )
    }
}

@PreviewLightDark
@Composable
private fun DLLSearchBarEmptyPreview() {
    DLLPreviewTheme {
        DLLSearchBar(
            query = "",
            onQueryChange = {},
            onClose = {},
            placeholder = "Search for deeplinks and folders",
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun DLLSearchBarFilledPreview() {
    DLLPreviewTheme {
        DLLSearchBar(
            query = "example.com",
            onQueryChange = {},
            onClose = {},
            placeholder = "Search for deeplinks and folders",
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun DLLSwitchEnabledPreview() {
    DLLPreviewTheme {
        DLLSwitchStates(enabled = true)
    }
}

@PreviewLightDark
@Composable
private fun DLLSwitchDisabledPreview() {
    DLLPreviewTheme {
        DLLSwitchStates(enabled = false)
    }
}

@PreviewLightDark
@Composable
private fun DLLSingleChoiceSegmentedButtonRowDefaultPreview() {
    DLLPreviewTheme {
        DLLSingleChoiceSegmentedButtonRow(
            modifier = Modifier.padding(16.dp),
            options = persistentListOf("JSON", "TXT"),
            selectedOption = "JSON",
            onOptionSelected = {},
        )
    }
}

@Composable
private fun DLLSwitchStates(enabled: Boolean) {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DLLSwitch(
            checked = false,
            onCheckedChange = {},
            enabled = enabled,
        )

        DLLSwitch(
            checked = true,
            onCheckedChange = {},
            enabled = enabled,
        )
    }
}
