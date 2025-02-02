package dev.koga.deeplinklauncher.settings.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.LaunchedEffect
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
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronRight
import compose.icons.tablericons.ExternalLink
import dev.koga.deeplinklauncher.LocalRootNavigator
import dev.koga.deeplinklauncher.SharedScreen
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.DLLTopBarDefaults
import dev.koga.deeplinklauncher.platform.Platform
import dev.koga.deeplinklauncher.platform.currentPlatform
import dev.koga.deeplinklauncher.purchase.ui.ProductsBottomSheet
import dev.koga.deeplinklauncher.settings.ui.sheets.AppThemeBottomSheet
import dev.koga.deeplinklauncher.settings.ui.sheets.DeleteDataBottomSheet
import dev.koga.deeplinklauncher.settings.ui.sheets.OpenSourceLicensesSheet
import dev.koga.deeplinklauncher.settings.ui.sheets.SuggestionsOptionBottomSheet
import dev.koga.resources.Res
import dev.koga.resources.ic_chevron_right_24dp
import dev.koga.resources.ic_launch_24dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

class SettingsScreen : Screen {

    enum class BottomSheetType {
        DELETE_DATA,
        APP_THEME,
        SUGGESTIONS_OPTION,
        PRODUCTS,
        OPEN_SOURCE,
    }

    @Composable
    override fun Content() {
        val navigator = LocalRootNavigator.current
        val screenModel = getScreenModel<SettingsScreenModel>()

        val preferences by screenModel.preferences.collectAsState()
        val products by screenModel.products.collectAsState()

        val scope = rememberCoroutineScope()
        val importScreen = rememberScreen(SharedScreen.ImportDeepLinks)
        val exportScreen = rememberScreen(SharedScreen.ExportDeepLinks)
        val snackBarHostState = remember { SnackbarHostState() }
        var bottomSheetType by rememberSaveable { mutableStateOf<BottomSheetType?>(null) }

        when (bottomSheetType) {
            BottomSheetType.DELETE_DATA -> DeleteDataBottomSheet(
                onDismissRequest = { bottomSheetType = null },
                onDeleteAll = {
                    bottomSheetType = null
                    screenModel.deleteAllData()
                    scope.launch {
                        snackBarHostState.showSnackbar("All data deleted")
                    }
                },
                onDeleteDeepLinks = {
                    bottomSheetType = null
                    screenModel.deleteAllDeepLinks()
                    scope.launch {
                        snackBarHostState.showSnackbar("Deeplinks deleted")
                    }
                },
                onDeleteFolders = {
                    bottomSheetType = null
                    screenModel.deleteAllFolders()
                    scope.launch {
                        snackBarHostState.showSnackbar("Folders deleted")
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

            BottomSheetType.PRODUCTS -> ProductsBottomSheet(
                products = products,
                onDismissRequest = { bottomSheetType = null },
                onSelectProduct = {
                    bottomSheetType = null
                    screenModel.purchaseProduct(it)
                },
            )

            BottomSheetType.OPEN_SOURCE -> OpenSourceLicensesSheet {
                bottomSheetType = null
            }

            null -> Unit
        }

        LaunchedEffect(true) {
            screenModel.messages.collectLatest {
                scope.launch {
                    snackBarHostState.showSnackbar(it)
                }
            }
        }

        SettingsScreenUI(
            snackbarHostState = snackBarHostState,
            isPurchaseAvailable = screenModel.isPurchaseAvailable,
            onBack = navigator::pop,
            onNavigateToExport = { navigator.push(exportScreen) },
            onNavigateToImport = { navigator.push(importScreen) },
            onShowDeleteDataBottomSheet = { bottomSheetType = BottomSheetType.DELETE_DATA },
            onNavigateToStore = screenModel::navigateToStore,
            onNavigateToOpenSourceLicenses = { bottomSheetType = BottomSheetType.OPEN_SOURCE },
            onNavigateToGithub = screenModel::navigateToGithub,
            onShowAppTheme = { bottomSheetType = BottomSheetType.APP_THEME },
            onShowSuggestionsOption = { bottomSheetType = BottomSheetType.SUGGESTIONS_OPTION },
            onShowProducts = { bottomSheetType = BottomSheetType.PRODUCTS },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenUI(
    snackbarHostState: SnackbarHostState,
    isPurchaseAvailable: Boolean,
    onBack: () -> Unit,
    onNavigateToExport: () -> Unit,
    onNavigateToImport: () -> Unit,
    onNavigateToStore: () -> Unit,
    onNavigateToOpenSourceLicenses: () -> Unit,
    onNavigateToGithub: () -> Unit,
    onShowDeleteDataBottomSheet: () -> Unit,
    onShowAppTheme: () -> Unit,
    onShowSuggestionsOption: () -> Unit,
    onShowProducts: () -> Unit,
) {
    Scaffold(
        topBar = {
            DLLTopBar(
                title = { },
                navigationIcon = {
                    DLLTopBarDefaults.navigationIcon(onClicked = onBack)
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
                            imageVector = TablerIcons.ChevronRight,
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
                            imageVector = TablerIcons.ChevronRight,
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
                            imageVector = TablerIcons.ChevronRight,
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
                            imageVector = TablerIcons.ChevronRight,
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
                            imageVector = TablerIcons.ChevronRight,
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

            if (isPurchaseAvailable) {
                item {
                    SettingsListItem(
                        title = "Buy me a coffee!",
                        description = "Check out the source code on GitHub and contribute!",
                        onClick = onShowProducts,
                        trailingContent = {
                            Icon(
                                imageVector = TablerIcons.ChevronRight,
                                contentDescription = "navigate",
                            )
                        },
                    )
                }
            }

            item {
                SettingsListItem(
                    title = "This project is open-source!",
                    description = "Check out the source code on GitHub and contribute!",
                    onClick = onNavigateToGithub,
                    trailingContent = {
                        Icon(
                            imageVector = TablerIcons.ExternalLink,
                            contentDescription = "launch",
                        )
                    },
                )
            }

            item {
                when (currentPlatform) {
                    Platform.ANDROID -> SettingsListItem(
                        title = "Review on the Play Store",
                        description = "Enjoying the app? Please leave a review. Your feedback helps a lot!",
                        onClick = onNavigateToStore,
                        trailingContent = {
                            Icon(
                                imageVector = TablerIcons.ExternalLink,
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
                                imageVector = TablerIcons.ExternalLink,
                                contentDescription = "Open Play Store",
                            )
                        },
                    )

                    Platform.IOS -> Unit
                }
            }

            item {
                SettingsListItem(
                    title = "Open-source licenses",
                    description = "View the open-source licenses for the libraries that make this app possible",
                    onClick = onNavigateToOpenSourceLicenses,
                    trailingContent = {
                        Icon(
                            imageVector = TablerIcons.ChevronRight,
                            contentDescription = "navigate",
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun SettingsListItem(
    title: String,
    description: String,
    trailingContent: @Composable () -> Unit,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 12.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Normal,
                ),
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        trailingContent()

    }

}
