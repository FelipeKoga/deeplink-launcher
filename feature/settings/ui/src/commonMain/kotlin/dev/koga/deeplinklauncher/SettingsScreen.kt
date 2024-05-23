package dev.koga.deeplinklauncher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import dev.koga.deeplinklauncher.components.AppThemeBottomSheet
import dev.koga.deeplinklauncher.components.DeleteDataBottomSheet
import dev.koga.deeplinklauncher.components.OpenSourceLicensesScreen
import dev.koga.deeplinklauncher.components.SuggestionsOptionBottomSheet
import dev.koga.resources.MR
import kotlinx.coroutines.launch

class SettingsScreen : Screen {

    enum class BottomSheetType {
        DELETE_DATA,
        APP_THEME,
        SUGGESTIONS_OPTION,
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<SettingsScreenModel>()

        val preferences by screenModel.preferences.collectAsState()

        val scope = rememberCoroutineScope()
        val importScreen = rememberScreen(SharedScreen.ImportDeepLinks)
        val exportScreen = rememberScreen(SharedScreen.ExportDeepLinks)
        val snackbarHostState = remember { SnackbarHostState() }

        var bottomSheetType by rememberSaveable { mutableStateOf<BottomSheetType?>(null) }

        when (bottomSheetType) {
            BottomSheetType.DELETE_DATA -> DeleteDataBottomSheet(
                onDismissRequest = { bottomSheetType = null },
                onDeleteAll = {
                    bottomSheetType = null
                    screenModel.deleteAllData()
                    scope.launch {
                        snackbarHostState.showSnackbar("All data deleted")
                    }
                },
                onDeleteDeepLinks = {
                    bottomSheetType = null
                    screenModel.deleteAllDeepLinks()
                    scope.launch {
                        snackbarHostState.showSnackbar("Deeplinks deleted")
                    }
                },
                onDeleteFolders = {
                    bottomSheetType = null
                    screenModel.deleteAllFolders()
                    scope.launch {
                        snackbarHostState.showSnackbar("Folders deleted")
                    }
                },
            )

            BottomSheetType.APP_THEME -> AppThemeBottomSheet(
                appTheme = preferences.appTheme,
                onDismissRequest = { bottomSheetType = null },
                onChange = {
                    screenModel.changeTheme(it)
                    bottomSheetType = null
                },
            )

            BottomSheetType.SUGGESTIONS_OPTION -> SuggestionsOptionBottomSheet(
                enabled = !preferences.shouldDisableDeepLinkSuggestions,
                onDismissRequest = { bottomSheetType = null },
                onChange = { screenModel.changeSuggestionsPreference(it) },
            )

            null -> Unit
        }

        SettingsScreenUI(
            snackbarHostState = snackbarHostState,
            appVersion = screenModel.appVersion,
            onBack = navigator::pop,
            onNavigateToExport = { navigator.push(exportScreen) },
            onNavigateToImport = { navigator.push(importScreen) },
            onShowDeleteDataBottomSheet = { bottomSheetType = BottomSheetType.DELETE_DATA },
            onNavigateToStore = screenModel::navigateToStore,
            onNavigateToOpenSourceLicenses = { navigator.push(OpenSourceLicensesScreen()) },
            onNavigateToGithub = screenModel::navigateToGithub,
            onShowAppTheme = { bottomSheetType = BottomSheetType.APP_THEME },
            onShowSuggestionsOption = { bottomSheetType = BottomSheetType.SUGGESTIONS_OPTION },
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
    onShowAppTheme: () -> Unit,
    onShowSuggestionsOption: () -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            DLLTopBar(
                title = "",
                onNavigationActionClicked = onBack,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
        ) {
            item { Spacer(modifier = Modifier.height(12.dp)) }

            item {
                Text(
                    text = "Settings",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    ),
                )
            }

            item {
                SettingsListItem(
                    title = "Theme",
                    description = "Customize the appearance of the app",
                    onClick = onShowAppTheme,
                    trailingContent = {
                        Icon(
                            painter = painterResource(MR.images.ic_chevron_right_24dp),
                            contentDescription = "navigate",
                        )
                    },
                )
            }

            item {
                SettingsListItem(
                    title = "Suggestions",
                    description = "Enable or disable deeplink suggestions when typing a deeplink",
                    onClick = onShowSuggestionsOption,
                    trailingContent = {
                        Icon(
                            painter = painterResource(MR.images.ic_chevron_right_24dp),
                            contentDescription = "navigate",
                        )
                    },
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
                            contentDescription = "navigate",
                        )
                    },
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
                            contentDescription = "navigate",
                        )
                    },
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
                            contentDescription = "navigate",
                        )
                    },
                )
            }

            item {
                DLLHorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            }

            item {
                Text(
                    text = "About",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
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
                            contentDescription = "launch",
                        )
                    },
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
                            contentDescription = "navigate",
                        )
                    },
                )
            }

            item {
                SettingsListItem(
                    title = "Open-source licenses",
                    description = "View the open-source licenses for the libraries that make this app possible",
                    onClick = onNavigateToOpenSourceLicenses,
                    trailingContent = {
                        Icon(
                            painter = painterResource(MR.images.ic_chevron_right_24dp),
                            contentDescription = "navigate",
                        )
                    },
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
                            contentDescription = "copy",
                        )
                    },
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
    onClick: () -> Unit = {},
) {
    ListItem(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
        ),
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
        },
        supportingContent = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Normal,
                ),
            )
        },
        trailingContent = trailingContent,
    )
}
