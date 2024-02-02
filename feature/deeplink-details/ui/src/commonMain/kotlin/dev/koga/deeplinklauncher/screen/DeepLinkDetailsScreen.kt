package dev.koga.deeplinklauncher.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.resources.compose.painterResource
import dev.koga.deeplinklauncher.DLLTopBar
import dev.koga.deeplinklauncher.component.DeleteDeepLinkConfirmationBottomSheet
import dev.koga.deeplinklauncher.folder.EditableText
import dev.koga.deeplinklauncher.folder.SelectFolderBottomSheet
import dev.koga.deeplinklauncher.model.Folder
import dev.koga.resources.MR
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

class DeepLinkDetailsScreen(private val deepLinkId: String) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val screenModel = getScreenModel<DeepLinkDetailScreenModel>(
            parameters = { parametersOf(deepLinkId) },
        )

        val uiState by screenModel.uiState.collectAsState()

        if (uiState.form.deleted) {
            navigator.pop()
        }

        var showDeleteDeepLinkConfirmation by remember {
            mutableStateOf(false)
        }

        if (showDeleteDeepLinkConfirmation) {
            DeleteDeepLinkConfirmationBottomSheet(onDismissRequest = {
                showDeleteDeepLinkConfirmation = false
            }, onDelete = {
                screenModel.delete()
                showDeleteDeepLinkConfirmation = false
            })
        }

        Scaffold(
            topBar = {
                DLLTopBar(title = "", onBack = navigator::pop, actions = {
                    FilledTonalIconButton(
                        onClick = { showDeleteDeepLinkConfirmation = true },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = Color.Red.copy(alpha = .2f),
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete",
                        )
                    }
                })
            },
            containerColor = MaterialTheme.colorScheme.surface,
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { contentPadding ->
            DeepLinkDetailsScreenContent(
                modifier = Modifier.padding(contentPadding),
                uiState = uiState,
                onNameChanged = screenModel::updateDeepLinkName,
                onDescriptionChanged = screenModel::updateDeepLinkDescription,
                onShare = screenModel::share,
                onFavorite = screenModel::favorite,
                onLaunch = screenModel::launch,
                onAddFolder = screenModel::insertFolder,
                onSelectFolder = screenModel::selectFolder,
                onRemoveFolder = screenModel::removeFolderFromDeepLink,
                onCopy = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Copied to clipboard",
                        )
                    }
                },
            )
        }
    }
}

@Composable
fun DeepLinkDetailsScreenContent(
    modifier: Modifier,
    uiState: DeepLinkDetailsUiState,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onShare: () -> Unit,
    onFavorite: () -> Unit,
    onLaunch: () -> Unit,
    onAddFolder: (String, String) -> Unit,
    onSelectFolder: (Folder) -> Unit,
    onRemoveFolder: () -> Unit,
    onCopy: () -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current

    val form = uiState.form

    var showSelectFolderBottomSheet by remember {
        mutableStateOf(false)
    }

    if (showSelectFolderBottomSheet) {
        SelectFolderBottomSheet(
            folders = uiState.folders,
            onDismissRequest = {
                showSelectFolderBottomSheet = false
            },
            onAdd = { name, description ->
                onAddFolder(name, description)
                showSelectFolderBottomSheet = false
            },
            onClick = {
                onSelectFolder(it)
                showSelectFolderBottomSheet = false
            },
        )
    }

    SelectionContainer(
        modifier = modifier,
    ) {
        Column(
            Modifier
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "Name",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Light,
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            EditableText(
                value = form.name,
                onSave = onNameChanged,
                modifier = Modifier.fillMaxWidth(),
                inputLabel = "Enter a name",
            ) {
                Text(
                    text = form.name.ifEmpty { "--" },
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Description",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Light,
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            EditableText(
                value = form.description,
                onSave = onDescriptionChanged,
                modifier = Modifier.fillMaxWidth(),
                inputLabel = "Enter a description",
            ) {
                Text(
                    text = form.description.ifEmpty { "--" },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedContent(
                targetState = form.folder,
                label = "",
                modifier = Modifier.fillMaxWidth(),
            ) { folder ->

                when (folder) {
                    null -> ElevatedAssistChip(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = { showSelectFolderBottomSheet = true },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "",
                                modifier = Modifier.size(18.dp),
                            )
                        },
                        label = { Text(text = "Add Folder") },
                    )

                    else -> ElevatedAssistChip(
                        colors = AssistChipDefaults.elevatedAssistChipColors(),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showSelectFolderBottomSheet = true },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(MR.images.ic_folder_24dp),
                                contentDescription = null,
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "Remove folder",
                                modifier = Modifier.clickable {
                                    onRemoveFolder()
                                },
                            )
                        },
                        label = {
                            Text(
                                text = folder.name,
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold,
                            )
                        },
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = form.link,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                    ),
                    modifier = Modifier.weight(1f),
                )

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(form.link))
                        onCopy()
                    },
                ) {
                    Icon(
                        painter = painterResource(MR.images.ic_content_copy_24dp),
                        contentDescription = "Copy",
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Divider()

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
            ) {
                IconButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = "Share",
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(onClick = onFavorite) {
                    Icon(
                        imageVector = if (form.isFavorite) {
                            Icons.Rounded.Favorite
                        } else {
                            Icons.Rounded.FavoriteBorder
                        },
                        contentDescription = "Favorite",
                        tint = if (form.isFavorite) {
                            Color.Red
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                FilledTonalIconButton(onClick = onLaunch) {
                    Icon(
                        painter = painterResource(MR.images.ic_launch_24dp),
                        contentDescription = "Launch",
                    )
                }
            }

            Spacer(modifier = Modifier.statusBarsPadding())
        }
    }
}
