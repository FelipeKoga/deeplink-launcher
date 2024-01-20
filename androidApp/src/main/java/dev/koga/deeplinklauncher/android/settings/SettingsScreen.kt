package dev.koga.deeplinklauncher.android.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.koga.deeplinklauncher.android.core.designsystem.DLLTopBar

@OptIn(ExperimentalMaterial3Api::class)
object SettingsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            topBar = {
                DLLTopBar(
                    title = "Settings",
                    onBack = navigator::pop
                )
            },
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            SettingsScreenContent(modifier = Modifier.padding(it))
        }
    }
}

@Composable
private fun SettingsScreenContent(
    modifier: Modifier,
) {
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SettingsItem(title = "Preferences", "Customize how things should work for you") {}
        SettingsItem(
            title = "Questions and Feedbacks",
            "Share your thoughts and questions with us"
        ) {}
        SettingsItem(title = "About", "Lean more about DeepLink Launcher!") {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesBottomSheet(
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = { /*TODO*/ }) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsAndFeedbacksBottomSheet(
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = { /*TODO*/ }) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutBottomSheet(
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = { /*TODO*/ }) {

    }
}


@Composable
fun SettingsItem(
    title: String,
    supportContent: String,
    onClick: () -> Unit,
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        modifier = Modifier.clickable { onClick() },
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
        },
        supportingContent = {
            Text(
                text = supportContent,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
        },
    )
}

