package dev.koga.deeplinklauncher.settings.impl

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronRight
import compose.icons.tablericons.ExternalLink
import dev.koga.deeplinklauncher.datatransfer.ui.navigation.DataTransferRoute
import dev.koga.deeplinklauncher.designsystem.DLLHorizontalDivider
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.DLLTopBarDefaults
import dev.koga.deeplinklauncher.platform.Platform
import dev.koga.deeplinklauncher.platform.currentPlatform
import dev.koga.deeplinklauncher.settings.impl.navigation.SettingsRoute

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
    Scaffold(
        topBar = {
            DLLTopBar(
                title = { },
                navigationIcon = {
                    DLLTopBarDefaults.navigationIcon(onClicked = onBack)
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
