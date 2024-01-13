package dev.koga.deeplinklauncher.android.deeplink.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.koga.deeplinklauncher.android.R
import dev.koga.deeplinklauncher.android.core.designsystem.DLLTopBar
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.StringQualifier


class DeepLinkDetailsScreen(private val deepLinkId: String) : Screen {

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel = getScreenModel<DeepLinkDetailScreenModel>(
            parameters = { parametersOf(deepLinkId) }
        )

        val details by screenModel.details.collectAsState()

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
            )
        }
    }
}

@Composable
fun DeepLinkDetailsScreenContent(
    modifier: Modifier,
    details: DeepLinkDetails,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit,
    onFavorite: () -> Unit,
    onLaunch: () -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current

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
                    modifier = Modifier.size(24.dp)
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_round_content_copy_24),
                        contentDescription = "Copy",
                        modifier = Modifier.size(24.dp)
                    )
                }

            }

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = details.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                onValueChange = onNameChanged,
                label = {
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = details.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 82.dp)
                    .clip(RoundedCornerShape(12.dp)),
                onValueChange = onDescriptionChanged,
                label = {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            ElevatedAssistChip(
                onClick = { /*TODO*/ },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "",
                        modifier = Modifier.size(18.dp),
                    )
                },
                label = { Text(text = "Add Folder") }
            )

            Spacer(modifier = Modifier.height(24.dp))


            Spacer(modifier = Modifier.weight(1f))

            Divider()

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {

                FilledTonalIconButton(
                    onClick = onDelete,
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