package dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import compose.icons.TablerIcons
import compose.icons.tablericons.Copy
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.state.DeepLinkDetailsUiState
import dev.koga.deeplinklauncher.designsystem.DLLSmallChip
import dev.koga.deeplinklauncher.designsystem.button.DLLIconButton
import kotlinx.coroutines.delay

@Composable
fun LaunchModeUI(
    modifier: Modifier,
    showFolder: Boolean,
    uiState: DeepLinkDetailsUiState,
    onFolderClicked: () -> Unit = {},
) {
    val clipboardManager = LocalClipboardManager.current

    var showCopyPopUp by remember { mutableStateOf(false) }
    var copyCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val copyCoordinatesOffset by remember(copyCoordinates) {
        derivedStateOf {
            val extraPadding = 12
            val position = copyCoordinates?.positionInParent()
            val height = copyCoordinates?.size?.height ?: 0

            IntOffset(
                x = position?.x?.toInt() ?: 0,
                y = (position?.y?.toInt() ?: 0) + height + extraPadding,
            )
        }
    }

    LaunchedEffect(showCopyPopUp) {
        if (showCopyPopUp) {
            delay(2000)
            showCopyPopUp = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        if (!uiState.deepLink.name.isNullOrBlank()) {
            Text(
                text = uiState.deepLink.name.orEmpty(),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        }

        if (!uiState.deepLink.description.isNullOrBlank()) {
            Spacer(modifier = Modifier.padding(top = 2.dp))

            Text(
                text = uiState.deepLink.description.orEmpty(),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            )
        }

        Spacer(modifier = Modifier.padding(top = 12.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            if (showCopyPopUp) {
                Popup(
                    offset = copyCoordinatesOffset,
                    properties = PopupProperties(
                        focusable = false,
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true,
                    ),
                ) {
                    Text(
                        text = "Copied!",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                        ),
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(12.dp),
                            )
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = uiState.deepLink.link,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    ),
                )

                DLLIconButton(
                    modifier = Modifier.onGloballyPositioned {
                        copyCoordinates = it
                    },
                    onClick = {
                        clipboardManager.setText(AnnotatedString(uiState.deepLink.link))
                        showCopyPopUp = true
                    },
                ) {
                    Icon(
                        imageVector = TablerIcons.Copy,
                        contentDescription = "Copy Link",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        if (showFolder && uiState.deepLink.folder != null) {
            Spacer(modifier = Modifier.padding(top = 24.dp))

            DLLSmallChip(
                label = uiState.deepLink.folder!!.name,
                onClick = onFolderClicked,
            )
        }
    }
}
