package dev.koga.deeplinklauncher

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import dev.koga.deeplinklauncher.button.DLLIconButton

@Composable
fun DLLNavigationIcon(modifier: Modifier = Modifier, onClicked: () -> Unit) {
    DLLIconButton(
        modifier = modifier,
        onClick = onClicked,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = "Back",
        )
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
        containerColor = MaterialTheme.colorScheme.background,
        scrolledContainerColor = MaterialTheme.colorScheme.background,
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DLLTopBar(
    modifier: Modifier = Modifier,
    title: String = "",
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit) = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        scrolledContainerColor = MaterialTheme.colorScheme.background,
    ),
) {
    DLLTopBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = colors,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
    )
}
