package dev.koga.deeplinklauncher.screen.details.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import dev.koga.deeplinklauncher.button.DLLIconButton
import dev.koga.deeplinklauncher.platform.canShareContent
import dev.koga.resources.Res
import dev.koga.resources.ic_content_copy_24dp
import dev.koga.resources.ic_duplicate_24dp
import dev.koga.resources.ic_launch_24dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
fun DeepLinkDetailsBottomBar(
    link: String,
    isFavorite: Boolean,
    onShare: () -> Unit,
    onFavorite: () -> Unit,
    onLaunch: () -> Unit,
    onDuplicate: () -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current

    var showCopyPopUp by remember { mutableStateOf(false) }
    var copyCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val copyCoordinatesOffset by remember(copyCoordinates) {
        derivedStateOf {
            val extraPadding = 62
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

    Box(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = showCopyPopUp,
            enter = fadeIn(initialAlpha = 0.3f),
            exit = fadeOut(targetAlpha = 0.0f),
        ) {
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

        BottomAppBar(
            containerColor = Color.Transparent,
            actions = {
                DLLIconButton(onClick = onDuplicate) {
                    Icon(
                        painterResource(Res.drawable.ic_duplicate_24dp),
                        contentDescription = "Duplicate",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }

                if (canShareContent) {
                    DLLIconButton(onClick = onShare) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                DLLIconButton(
                    modifier = Modifier.onGloballyPositioned {
                        copyCoordinates = it
                    },
                    onClick = {
                        clipboardManager.setText(AnnotatedString(link))
                        showCopyPopUp = true
                    },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_content_copy_24dp),
                        contentDescription = "Copy Link",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }

                DLLIconButton(onClick = onFavorite) {
                    Icon(
                        imageVector = if (isFavorite) {
                            Icons.Rounded.Favorite
                        } else {
                            Icons.Rounded.FavoriteBorder
                        },
                        contentDescription = "Favorite",
                        tint = if (isFavorite) {
                            Color(0xFFE57373)
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                    )
                }
            },
            floatingActionButton = {
                Button(onClick = onLaunch) {
                    Text(
                        text = "Launch",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        painter = painterResource(Res.drawable.ic_launch_24dp),
                        contentDescription = "Launch",
                        modifier = Modifier.size(18.dp),
                    )
                }
            },
        )
    }
}
