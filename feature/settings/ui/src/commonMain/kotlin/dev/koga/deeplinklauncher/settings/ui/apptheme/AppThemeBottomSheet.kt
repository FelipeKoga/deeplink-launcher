package dev.koga.deeplinklauncher.settings.ui.apptheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Check
import dev.koga.deeplinklauncher.designsystem.DLLModalBottomSheet
import dev.koga.deeplinklauncher.preferences.api.model.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppThemeBottomSheet(
    viewModel: AppThemeViewModel,
    onDismissRequest: () -> Unit,
) {
    val appTheme = viewModel.appTheme

    DLLModalBottomSheet(onDismiss = onDismissRequest) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Theme",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Choose an option and press to confirm.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                ),
            )

            Spacer(modifier = Modifier.height(24.dp))

            AppTheme.entries.forEach {
                AppThemeListItem(
                    label = when (it) {
                        AppTheme.LIGHT -> "Light"
                        AppTheme.DARK -> "Dark"
                        AppTheme.AUTO -> "Auto"
                    },
                    selected = appTheme == it,
                    onClick = {
                        viewModel.update(it)
                        onDismissRequest()
                    },
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun AppThemeListItem(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        leadingContent = {
            if (selected) {
                Icon(
                    imageVector = TablerIcons.Check,
                    contentDescription = null,
                )
            }
        },
        headlineContent = {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
        },
    )
}
