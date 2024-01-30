package dev.koga.deeplinklauncher

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.koga.deeplinklauncher.component.DeleteFolderBottomSheet
import dev.koga.deeplinklauncher.deeplink.DeepLinkItem
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.provider.DeepLinkClipboardProvider
import dev.koga.deeplinklauncher.theme.LocalDimensions
import dev.koga.deeplinklauncher.usecase.deeplink.LaunchDeepLink
import dev.koga.navigation.SharedScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

class FolderDetailsScreen(private val folderId: String) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel = getScreenModel<FolderDetailsScreenModel>(
            parameters = { parametersOf(folderId) },
        )

        val launchDeepLink = koinInject<LaunchDeepLink>()
        val deepLinkClipboardProvider = koinInject<DeepLinkClipboardProvider>()

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.deletedEvent.collectLatest { navigator.pop() }
        }

        var showDeleteDialog by remember { mutableStateOf(false) }
        if (showDeleteDialog) {
            DeleteFolderBottomSheet(
                onDismissRequest = { showDeleteDialog = false },
                onDelete = {
                    showDeleteDialog = false
                    screenModel.delete()
                },
            )
        }

        Scaffold(
            topBar = {
                DLLTopBar(onBack = navigator::pop, actions = {
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = Color.Red.copy(alpha = .2f),
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(18.dp),
                        )
                    }
                })
            },
            containerColor = MaterialTheme.colorScheme.surface,
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
            ) {
                FolderDetailsScreenContent(
                    form = state,
                    onEditName = screenModel::updateName,
                    onEditDescription = screenModel::updateDescription,
                    onDeepLinkClick = { deepLink ->
                        val screen = ScreenRegistry.get(SharedScreen.DeepLinkDetails(deepLink.id))
                        navigator.push(screen)
                    },
                    onDeepLinkLaunch = { deepLink ->
                        launchDeepLink.launch(deepLink.link)
                    },
                    onDeepLinkCopy = {
                        scope.launch {
                            deepLinkClipboardProvider.copy(it.link)
                            snackbarHostState.showSnackbar(
                                message = "Copied to clipboard",
                                actionLabel = "Dismiss",
                            )
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun FolderDetailsScreenContent(
    form: FolderDetails,
    onEditName: (String) -> Unit,
    onEditDescription: (String) -> Unit,
    onDeepLinkClick: (DeepLink) -> Unit,
    onDeepLinkLaunch: (DeepLink) -> Unit,
    onDeepLinkCopy: (DeepLink) -> Unit,
) {

    val dimensions = LocalDimensions.current

    Column(modifier = Modifier.padding(horizontal = dimensions.extraLarge)) {
        Spacer(modifier = Modifier.height(dimensions.extraLarge))

        FolderNameContent(
            name = form.name,
            onEditName = onEditName,
        )

        Spacer(modifier = Modifier.height(dimensions.extraLarge))

        FolderDescriptionContent(
            description = form.description,
            onEditDescription = onEditDescription,
        )

        Divider(modifier = Modifier.padding(vertical = dimensions.extraLarge))

        FolderDeepLinks(
            deepLinks = form.deepLinks,
            onClick = onDeepLinkClick,
            onLaunch = onDeepLinkLaunch,
            onCopy = onDeepLinkCopy
        )
    }
}


@Composable
fun FolderNameContent(
    name: String,
    onEditName: (String) -> Unit,
) {
    val dimensions = LocalDimensions.current

    var showEditNameInput by rememberSaveable {
        mutableStateOf(false)
    }

    val folderName by rememberSaveable(name) {
        mutableStateOf(name)
    }

    AnimatedContent(
        targetState = showEditNameInput,
        label = "",
    ) { target ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            when (target) {
                true -> {
                    DLLTextField(
                        label = "Folder name",
                        value = folderName,
                        onValueChange = onEditName,
                        trailingIcon = {
                            IconButton(
                                onClick = { showEditNameInput = false },
                                modifier = Modifier.size(18.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Done,
                                    contentDescription = "Edit name",
                                )
                            }
                        },
                    )
                }

                false -> {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )

                    Spacer(modifier = Modifier.width(dimensions.mediumLarge))

                    IconButton(
                        onClick = { showEditNameInput = true },
                        modifier = Modifier.size(18.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.secondary,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit name",
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FolderDescriptionContent(
    description: String,
    onEditDescription: (String) -> Unit,
) {
    val dimensions = LocalDimensions.current

    var showEditDescriptionInput by rememberSaveable {
        mutableStateOf(false)
    }

    val folderDescription by rememberSaveable(description) {
        mutableStateOf(description)
    }

    AnimatedContent(
        targetState = showEditDescriptionInput,
        label = "",
    ) { target ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            when (target) {
                true -> {
                    DLLTextField(
                        label = "Folder description",
                        value = folderDescription,
                        onValueChange = onEditDescription,
                        trailingIcon = {
                            IconButton(
                                onClick = { showEditDescriptionInput = false },
                                modifier = Modifier.size(18.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Done,
                                    contentDescription = "Edit name",
                                )
                            }
                        },
                    )
                }

                false -> {
                    Text(
                        text = description.ifEmpty { "No description" },
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f),
                    )

                    Spacer(modifier = Modifier.width(dimensions.mediumLarge))

                    IconButton(
                        onClick = { showEditDescriptionInput = true },
                        modifier = Modifier.size(18.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.secondary,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit description",
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FolderDeepLinks(
    deepLinks: ImmutableList<DeepLink>,
    onClick: (DeepLink) -> Unit,
    onLaunch: (DeepLink) -> Unit,
    onCopy: (DeepLink) -> Unit,
) {
    val dimensions = LocalDimensions.current

    if (deepLinks.isEmpty()) {
        Text(
            text = "No deeplinks vinculated to this folder",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal,
            ),
        )
    } else {
        Text(
            text = "Deeplinks",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        Spacer(modifier = Modifier.height(dimensions.extraLarge))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensions.extraLarge),
        ) {
            items(deepLinks) { deepLink ->
                DeepLinkItem(
                    deepLink = deepLink,
                    onClick = onClick,
                    onLaunch = onLaunch,
                    onCopy = onCopy,
                )
            }

            item {
                Spacer(modifier = Modifier.height(dimensions.extraLarge))
            }
        }
    }

}
