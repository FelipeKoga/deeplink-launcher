package dev.koga.deeplinklauncher.android.folder.detail

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.koga.deeplinklauncher.android.core.designsystem.DLLTextField
import dev.koga.deeplinklauncher.android.core.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.android.deeplink.detail.DeepLinkDetailsScreen
import dev.koga.deeplinklauncher.android.deeplink.home.component.DeepLinkItem
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.usecase.LaunchDeepLink
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

class FolderDetailsScreen(private val folderId: String) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel = getScreenModel<FolderDetailsScreenModel>(
            parameters = { parametersOf(folderId) }
        )

        val launchDeepLink = koinInject<LaunchDeepLink>()

        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.deletedEvent.collectLatest { navigator.pop() }
        }

        var showDeleteDialog by remember { mutableStateOf(false) }
        if (showDeleteDialog) {
            ModalBottomSheet(
                onDismissRequest = { showDeleteDialog = false },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )
            ) {
                Column {
                    Text(
                        text = "Delete folder",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(24.dp)
                    )

                    HorizontalDivider()

                    Text(
                        text = "Are you sure you want to delete this deep link?",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(24.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {


                        TextButton(
                            onClick = { showDeleteDialog = false },
                            modifier = Modifier.padding(start = 12.dp)
                        ) {
                            Text(text = "Cancel", fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.width(24.dp))

                        FilledTonalButton(
                            onClick = {
                                showDeleteDialog = false
                                screenModel.delete()
                            },
                            modifier = Modifier.padding(end = 12.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = "Delete",
                                fontWeight = FontWeight.Bold
                            )
                        }


                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

        }

        Scaffold(
            topBar = {
                DLLTopBar(onBack = navigator::pop, actions = {
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = Color.Red.copy(alpha = .2f)
                        )
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
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                FolderDetailsScreenContent(
                    form = state,
                    onEditName = screenModel::updateName,
                    onEditDescription = screenModel::updateDescription,
                    onDeepLinkClick = { deepLink ->
                        navigator.push(
                            DeepLinkDetailsScreen(
                                deepLinkId = deepLink.id
                            )
                        )
                    },
                    onDeepLinkLaunch = { deepLink ->
                        launchDeepLink.launch(deepLink.link)
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
) {

    var showEditNameInput by rememberSaveable {
        mutableStateOf(false)
    }

    var showEditDescriptionInput by rememberSaveable {
        mutableStateOf(false)
    }

    val folderName by rememberSaveable(form.name) {
        mutableStateOf(form.name)
    }

    val folderDescription by rememberSaveable(form.description) {
        mutableStateOf(form.description)
    }

    Column(modifier = Modifier.padding(24.dp)) {
        AnimatedContent(
            targetState = showEditNameInput,
            label = ""
        ) { target ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
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
                                    modifier = Modifier.size(18.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Done,
                                        contentDescription = "Edit name"
                                    )
                                }
                            }
                        )
                    }

                    false -> {
                        Text(
                            text = form.name,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        IconButton(
                            onClick = { showEditNameInput = true },
                            modifier = Modifier.size(18.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = "Edit name"
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedContent(
            targetState = showEditDescriptionInput,
            label = ""
        ) { target ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
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
                                    modifier = Modifier.size(18.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Done,
                                        contentDescription = "Edit name"
                                    )
                                }
                            }
                        )
                    }

                    false -> {
                        Text(
                            text = form.description.ifEmpty { "No description" },
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        IconButton(
                            onClick = { showEditDescriptionInput = true },
                            modifier = Modifier.size(18.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = "Edit description"
                            )
                        }
                    }
                }
            }

        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

        if (form.deepLinks.isEmpty()) {
            Text(
                text = "No deeplinks vinculated to this folder",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal
                )
            )
        } else {
            Text(
                text = "Deeplinks",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.padding(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(form.deepLinks) { deepLink ->
                    DeepLinkItem(
                        deepLink = deepLink,
                        onClick = onDeepLinkClick,
                        onLaunch = onDeepLinkLaunch,
                    )
                }
            }
        }
    }
}