package dev.koga.deeplinklauncher.designsystem

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme

object DLLTopBarDefaults {

    @Composable
    fun Title(
        text: String,
        modifier: Modifier = Modifier,
    ) {
        val colors = DeepLinkTheme.colors
        Text(
            modifier = modifier,
            text = text,
            style = DeepLinkTheme.typography.title.topBar.copy(
                color = colors.text.primary,
            ),
        )
    }

    @Composable
    fun NavigationIcon(
        onClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        DLLIconButton(
            modifier = modifier,
            onClick = onClicked,
        ) {
            Icon(
                imageVector = TablerIcons.ArrowLeft,
                contentDescription = "Back",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DLLTopBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit) = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = DeepLinkTheme.colors.surface.background,
        scrolledContainerColor = DeepLinkTheme.colors.surface.background,
    ),
) {
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = colors,
        title = title,
        navigationIcon = {
            if (navigationIcon != null) navigationIcon()
        },
        actions = actions,
    )
}
