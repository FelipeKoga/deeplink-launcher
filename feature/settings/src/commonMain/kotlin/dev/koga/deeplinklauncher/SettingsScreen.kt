package dev.koga.deeplinklauncher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.resources.compose.painterResource
import dev.koga.deeplinklauncher.components.DeleteDataBottomSheet
import dev.koga.deeplinklauncher.components.OpenSourceLicensesScreen
import dev.koga.resources.MR
import kotlinx.coroutines.launch

class SettingsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<SettingsScreenModel>()
        val scope = rememberCoroutineScope()

        val importScreen = rememberScreen(SharedScreen.ImportDeepLinks)
        val exportScreen = rememberScreen(SharedScreen.ExportDeepLinks)
        val snackbarHostState = remember { SnackbarHostState() }

        var showDeleteDataBottomSheet by rememberSaveable { mutableStateOf(false) }

        if (showDeleteDataBottomSheet) {
            DeleteDataBottomSheet(
                onDismissRequest = { showDeleteDataBottomSheet = false },
                onDeleteAll = {
                    screenModel.deleteAllData()
                    showDeleteDataBottomSheet = false

                    scope.launch {
                        snackbarHostState.showSnackbar("All data deleted")
                    }
                },
                onDeleteDeepLinks = {
                    showDeleteDataBottomSheet = false
                    screenModel.deleteAllDeepLinks()

                    scope.launch {
                        snackbarHostState.showSnackbar("Deep links deleted")
                    }
                },
                onDeleteFolders = {
                    screenModel.deleteAllFolders()
                    showDeleteDataBottomSheet = false

                    scope.launch {
                        snackbarHostState.showSnackbar("Folders deleted")
                    }
                }

            )
        }

        SettingsScreenUI(
            snackbarHostState = snackbarHostState,
            appVersion = screenModel.appVersion,
            onBack = navigator::pop,
            onNavigateToExport = { navigator.push(importScreen) },
            onNavigateToImport = { navigator.push(exportScreen) },
            onShowDeleteDataBottomSheet = { showDeleteDataBottomSheet = true },
            onNavigateToStore = screenModel::navigateToStore,
            onNavigateToOpenSourceLicenses = { navigator.push(OpenSourceLicensesScreen()) },
            onNavigateToGithub = screenModel::navigateToGithub
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenUI(
    snackbarHostState: SnackbarHostState,
    appVersion: String,
    onBack: () -> Unit,
    onNavigateToExport: () -> Unit,
    onNavigateToImport: () -> Unit,
    onNavigateToStore: () -> Unit,
    onNavigateToOpenSourceLicenses: () -> Unit,
    onNavigateToGithub: () -> Unit,
    onShowDeleteDataBottomSheet: () -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            DLLTopBar(
                title = "",
                onBack = onBack
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                Text(
                    text = "Settings",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                )
            }

            item {
                SettingsListItem(
                    title = "Export",
                    description = "Export all your data to a file",
                    onClick = onNavigateToExport,
                    trailingContent = {
                        Icon(
                            painter = painterResource(MR.images.ic_chevron_right_24dp),
                            contentDescription = "navigate"
                        )
                    }
                )
            }

            item {
                SettingsListItem(
                    title = "Import",
                    description = "Import data from a file",
                    onClick = onNavigateToImport,
                    trailingContent = {
                        Icon(
                            painter = painterResource(MR.images.ic_chevron_right_24dp),
                            contentDescription = "navigate"
                        )
                    }
                )
            }

            item {
                SettingsListItem(
                    title = "Delete data",
                    description = "Choose between deleting all deeplinks, folder or both",
                    onClick = onShowDeleteDataBottomSheet,
                    trailingContent = {
                        Icon(
                            painter = painterResource(MR.images.ic_chevron_right_24dp),
                            contentDescription = "navigate"
                        )
                    }
                )
            }

            item {
                Divider(modifier = Modifier.padding(vertical = 12.dp))
            }

            item {
                Text(
                    text = "About",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                )
            }

            item {
                SettingsListItem(
                    title = "This project is open-source!",
                    description = "Check out the source code on GitHub and contribute!",
                    onClick = onNavigateToGithub,
                    trailingContent = {
                        Icon(
                            painter = painterResource(MR.images.ic_launch_24dp),
                            contentDescription = "launch"
                        )
                    }
                )
            }

            item {
                SettingsListItem(
                    title = "Review on the Play Store",
                    description = "If you like the app, please leave a review. It helps a lot!",
                    onClick = onNavigateToStore,
                    trailingContent = {
                        Icon(
                            painter = painterResource(MR.images.ic_launch_24dp),
                            contentDescription = "navigate"
                        )
                    }
                )
            }

            item {
                SettingsListItem(
                    title = "Open-source licenses",
                    description = "View the open-source licenses for the libraries used in this app",
                    onClick = onNavigateToOpenSourceLicenses,
                    trailingContent = {
                        Icon(
                            painter = painterResource(MR.images.ic_chevron_right_24dp),
                            contentDescription = "navigate"
                        )
                    }
                )
            }

            item {
                SettingsListItem(
                    title = "Version",
                    description = appVersion,
                    onClick = {
                        clipboardManager.setText(AnnotatedString(appVersion))
                        scope.launch {
                            snackbarHostState.showSnackbar("Version copied to clipboard")
                        }
                    },
                    trailingContent = {
                        Icon(
                            painter = painterResource(MR.images.ic_content_copy_24dp),
                            contentDescription = "copy"
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun SettingsListItem(
    title: String,
    description: String,
    trailingContent: @Composable () -> Unit,
    onClick: () -> Unit = {}
) {
    ListItem(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        supportingContent = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal
                )
            )
        },
        trailingContent = trailingContent
    )
}