package dev.koga.deeplinklauncher.android.deeplink.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.koga.deeplinklauncher.android.R
import dev.koga.deeplinklauncher.android.core.designsystem.DLLTextField
import dev.koga.deeplinklauncher.android.core.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.android.folder.SelectFolderBottomSheet
import dev.koga.deeplinklauncher.model.Folder
import org.koin.core.parameter.parametersOf


class DeepLinkDetailsScreen(private val deepLinkId: String) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel = getScreenModel<DeepLinkDetailScreenModel>(
            parameters = { parametersOf(deepLinkId) }
        )

        val details by screenModel.details.collectAsState()
        val folders by screenModel.folders.collectAsState()

        if (details.deleted) {
            navigator.pop()
        }

        Scaffold(
            topBar = {
                DLLTopBar(title = "", onBack = navigator::pop)
            },
            containerColor = MaterialTheme.colorScheme.surface,
        ) { contentPadding ->
            DeepLinkDetailsScreenContent(
                modifier = Modifier.padding(contentPadding),
                details = details,
                onNameChanged = screenModel::updateDeepLinkName,
                onDescriptionChanged = screenModel::updateDeepLinkDescription,
                onShare = screenModel::share,
                onDelete = screenModel::delete,
                onFavorite = screenModel::favorite,
                onLaunch = screenModel::launch,
                onAddFolder = screenModel::insertFolder,
                onSelectFolder = screenModel::selectFolder,
                onRemoveFolder = screenModel::removeFolderFromDeepLink,
                folders = folders,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeepLinkDetailsScreenContent(
    modifier: Modifier,
    details: DeepLinkDetails,
    folders: List<Folder>,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit,
    onFavorite: () -> Unit,
    onLaunch: () -> Unit,
    onAddFolder: (String, String) -> Unit,
    onSelectFolder: (Folder) -> Unit,
    onRemoveFolder: () -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var showDeleteDeepLinkConfirmation by remember {
        mutableStateOf(false)
    }

    var showSelectFolderBottomSheet by remember {
        mutableStateOf(false)
    }


    if (showDeleteDeepLinkConfirmation) {
        DeleteDeepLinkConfirmationBottomSheet(onDismissRequest = {
            showDeleteDeepLinkConfirmation = false
        }, onDelete = {
            onDelete()
            showDeleteDeepLinkConfirmation = false
        })
    }

    if (showSelectFolderBottomSheet) {
        SelectFolderBottomSheet(
            folders = folders,
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
            }
        )
    }

    SelectionContainer(
        modifier = modifier
    ) {
        Column(
            Modifier
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = details.link,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(details.link))
                    },
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_round_content_copy_24),
                        contentDescription = "Copy",
                    )
                }

            }

            Spacer(modifier = Modifier.height(32.dp))


            DLLTextField(label = "Name", value = details.name, onValueChange = onNameChanged)

            Spacer(modifier = Modifier.height(12.dp))

            DLLTextField(
                modifier = Modifier.defaultMinSize(minHeight = 120.dp),
                label = "Description",
                value = details.description,
                onValueChange = onDescriptionChanged,
                imeAction = ImeAction.Done,
                onDone = { keyboardController?.hide() }
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedContent(
                targetState = details.folder,
                label = "",
                modifier = Modifier.fillMaxWidth()
            ) { folder ->

                when (folder) {
                    null -> ElevatedAssistChip(
                        modifier = Modifier
                            .fillMaxWidth(), onClick = { showSelectFolderBottomSheet = true },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "",
                                modifier = Modifier.size(18.dp),
                            )
                        },
                        label = { Text(text = "Add Folder") }
                    )

                    else -> ElevatedAssistChip(
                        colors = AssistChipDefaults.elevatedAssistChipColors(),
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = { showSelectFolderBottomSheet = true },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_round_folder_24),
                                contentDescription = null,
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "Remove folder",
                                modifier = Modifier.clickable {
                                    onRemoveFolder()
                                }
                            )
                        },
                        label = {
                            Text(
                                text = folder.name,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    )
                }
            }


            Spacer(modifier = Modifier.height(24.dp))


            Spacer(modifier = Modifier.weight(1f))

            HorizontalDivider()

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {

                FilledTonalIconButton(
                    onClick = { showDeleteDeepLinkConfirmation = true },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = Color.Red.copy(alpha = .2f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete",
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = "Share",
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(onClick = onFavorite) {
                    Icon(
                        imageVector = if (details.isFavorite) Icons.Rounded.Favorite
                        else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (details.isFavorite) Color.Red
                        else MaterialTheme.colorScheme.onSurface,
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                FilledTonalIconButton(onClick = onLaunch) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_round_launch_24),
                        contentDescription = "Launch",
                    )
                }
            }

            Spacer(modifier = Modifier.statusBarsPadding())
        }
    }

}