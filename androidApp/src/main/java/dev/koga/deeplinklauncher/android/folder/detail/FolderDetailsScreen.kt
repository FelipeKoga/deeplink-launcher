package dev.koga.deeplinklauncher.android.folder.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import dev.koga.deeplinklauncher.android.deeplink.home.DeepLinkItem
import dev.koga.deeplinklauncher.model.DeepLink
import dev.koga.deeplinklauncher.model.Folder
import org.koin.core.parameter.parametersOf

class FolderDetailsScreen(private val folderId: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<FolderDetailsScreenModel>(
            parameters = { parametersOf(folderId) }
        )

        val state by screenModel.state.collectAsState()

        Scaffold(
            topBar = {
                DLLTopBar(onBack = navigator::pop, actions = {
                    IconButton(
                        onClick = {},
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
                when (state) {
                    is FolderDetailsScreenState.Loading -> Unit
                    is FolderDetailsScreenState.Loaded -> FolderDetailsScreenContent(
                        folder = (state as FolderDetailsScreenState.Loaded).folder,
                        deepLinks = (state as FolderDetailsScreenState.Loaded).deepLinks
                    )
                }
            }

        }


    }

    @Composable
    fun FolderDetailsScreenContent(
        folder: Folder,
        deepLinks: List<DeepLink>
    ) {

        Column(modifier = Modifier.padding(24.dp)) {

            Text(
                text = folder.name, style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            folder.description?.let { description ->
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (deepLinks.isEmpty()) {
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
                deepLinks.forEach { deepLink ->
                    DeepLinkItem(
                        deepLink = deepLink,
                        onClick = {},
                        openDetails = {}
                    )
                }
            }
        }
    }
}