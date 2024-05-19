package dev.koga.deeplinklauncher.screen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.DLLSearchBar

@Composable
internal fun HomeLaunchDeepLinkBottomSheetContent(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    launch: () -> Unit,
    errorMessage: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
    ) {
        Divider(thickness = .3.dp)
        Spacer(Modifier.height(12.dp))
        Column(
            modifier = Modifier.padding(
                start = 24.dp,
                end = 24.dp,
            ),
        ) {
            DLLSearchBar(
                value = value,
                onChanged = onValueChange,
                hint = "Enter deeplink here",
                onSearch = { launch() },
            )

            AnimatedVisibility(
                visible = errorMessage != null,
            ) {
                Text(
                    text = errorMessage.orEmpty(),
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
        }
    }
}
