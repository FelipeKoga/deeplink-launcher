package dev.koga.deeplinklauncher.designsystem

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton

object DLLTopBarDefaults {

    @Composable
    fun title(
        text: String,
        modifier: Modifier = Modifier,
    ) {
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
        )
    }

    @Composable
    fun navigationIcon(
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
