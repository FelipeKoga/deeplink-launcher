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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import dev.koga.deeplinklauncher.components.AppThemeBottomSheet
import dev.koga.deeplinklauncher.components.DeleteDataBottomSheet
import dev.koga.deeplinklauncher.components.OpenSourceLicensesScreen
import dev.koga.deeplinklauncher.components.SuggestionsOptionBottomSheet
import dev.koga.deeplinklauncher.platform.Platform
import dev.koga.deeplinklauncher.platform.platform
import dev.koga.resources.Res
import dev.koga.resources.ic_chevron_right_24dp
import dev.koga.resources.ic_launch_24dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

class SettingsScreen : Screen {

    enum class BottomSheetType {
        DELETE_DATA,
        APP_THEME,
        SUGGESTIONS_OPTION,
    }

    @Composable
    override fun Content() {
        val navigator = LocalRootNavigator.current
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
    Scaffold(
        topBar = {
            DLLTopBar(
                title = "",
                navigationIcon = {
                    DLLNavigationIcon(onClicked = onBack)
                },
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
                            painter = painterResource(Res.drawable.ic_chevron_right_24dp),
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
                            painter = painterResource(Res.drawable.ic_chevron_right_24dp),
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
                            painter = painterResource(Res.drawable.ic_chevron_right_24dp),
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
                            painter = painterResource(Res.drawable.ic_chevron_right_24dp),
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
                            painter = painterResource(Res.drawable.ic_chevron_right_24dp),
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
                            painter = painterResource(Res.drawable.ic_launch_24dp),
                            contentDescription = "launch",
                        )
                    },
                )
            }

            item {
                when (platform) {
                    Platform.ANDROID -> SettingsListItem(
                        title = "Review on the Play Store",
                        description = "Enjoying the app? Please leave a review. Your feedback helps a lot!",
                        onClick = onNavigateToStore,
                        trailingContent = {
                            Icon(
                                painter = painterResource(Res.drawable.ic_launch_24dp),
                                contentDescription = "navigate",
                            )
                        },
                    )

                    Platform.JVM -> SettingsListItem(
                        title = "Download our Android app!",
                        description = "Need the app on your phone? Get it now from the Play Store!",
                        onClick = onNavigateToStore,
                        trailingContent = {
                            Icon(
                                painter = painterResource(Res.drawable.ic_launch_24dp),
                                contentDescription = "Open Play Store",
                            )
                        },
                    )
                }
            }

            item {
                SettingsListItem(
                    title = "Open-source licenses",
                    description = "View the open-source licenses for the libraries that make this app possible",
                    onClick = onNavigateToOpenSourceLicenses,
                    trailingContent = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_chevron_right_24dp),
                            contentDescription = "navigate",
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
