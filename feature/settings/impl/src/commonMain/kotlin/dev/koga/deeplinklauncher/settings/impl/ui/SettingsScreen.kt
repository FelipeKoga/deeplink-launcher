package dev.koga.deeplinklauncher.settings.impl.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronRight
import compose.icons.tablericons.ExternalLink
import dev.koga.deeplinklauncher.datatransfer.api.ui.navigation.DataTransferRoute
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.DLLListItem
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.DLLTopBarDefaults
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import dev.koga.deeplinklauncher.platform.Platform
import dev.koga.deeplinklauncher.platform.currentPlatform
import dev.koga.deeplinklauncher.settings.impl.ui.navigation.SettingsRoute

@Composable
fun SettingsScreen(
    viewmodel: SettingsViewModel,
) {
    SettingsUI(
        isPurchaseAvailable = viewmodel.isPurchaseAvailable,
        onNavigateToStore = viewmodel::navigateToStore,
        onNavigateToGithub = viewmodel::navigateToGithub,
        onBack = {
            viewmodel.popBackStack()
        },
        onNavigateToExport = {
            viewmodel.navigate(DataTransferRoute.ExportData)
        },
        onNavigateToImport = {
            viewmodel.navigate(DataTransferRoute.ImportData)
        },
        onShowDeleteDataBottomSheet = {
            viewmodel.navigate(SettingsRoute.DeleteDataBottomSheet)
        },
        onShowAppTheme = {
            viewmodel.navigate(SettingsRoute.AppThemeBottomSheet)
        },
        onNavigateToOpenSourceLicenses = {
            viewmodel.navigate(SettingsRoute.OpenSourceLicenses)
        },
        onShowSuggestionsOption = {
            viewmodel.navigate(SettingsRoute.SuggestionsOptionBottomSheet)
        },
        onShowProducts = {
            viewmodel.navigate(SettingsRoute.ProductsBottomSheet)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsUI(
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
    val colors = DeepLinkTheme.colors
    val typography = DeepLinkTheme.typography

    Scaffold(
        topBar = {
            DLLTopBar(
                title = { },
                navigationIcon = {
                    DLLTopBarDefaults.NavigationIcon(onClicked = onBack)
                },
            )
        },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
        ) {
            item { Spacer(modifier = Modifier.height(12.dp)) }

            item {
                Text(
                    text = "Settings",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    style = typography.label.section.copy(
                        color = colors.text.placeholder,
                    ),
                )
            }

            item {
                DLLListItem(
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
                DLLListItem(
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
                DLLListItem(
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
                DLLListItem(
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
                DLLListItem(
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
                    style = typography.label.section.copy(
                        color = colors.text.placeholder,
                    ),
                )
            }

            if (isPurchaseAvailable) {
                item {
                    DLLListItem(
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
                DLLListItem(
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
                    Platform.ANDROID -> DLLListItem(
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

                    Platform.JVM -> DLLListItem(
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
                DLLListItem(
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
