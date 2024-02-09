package dev.koga.deeplinklauncher.deeplink

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import dev.icerock.moko.resources.compose.painterResource
import dev.koga.resources.MR
import kotlinx.coroutines.delay

@Composable
fun DeepLinkActionsRow(
    link: String,
    isFavorite: Boolean,
    onShare: () -> Unit,
    onFavorite: () -> Unit,
    onLaunch: () -> Unit,
    onDuplicate: () -> Unit,
) {

    val clipboardManager = LocalClipboardManager.current

    var showCopyPopUp by remember { mutableStateOf(false) }

    LaunchedEffect(showCopyPopUp) {
        if (showCopyPopUp) {
            delay(2000)
            showCopyPopUp = false
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
    ) {
        FilledTonalButton(onClick = onDuplicate) {
            Text(
                text = "Duplicate",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(onClick = onShare) {
            Icon(
                imageVector = Icons.Rounded.Share,
                contentDescription = "Share",
            )
        }

        IconButton(onClick = onFavorite) {
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(onClick = {
                clipboardManager.setText(AnnotatedString(link))
                showCopyPopUp = true
            }) {
                Icon(
                    painter = painterResource(MR.images.ic_content_copy_24dp),
                    contentDescription = "Copy Link",
                )
            }

            AnimatedVisibility(
                visible = showCopyPopUp,
                enter = fadeIn(initialAlpha = 0.3f), // Customize as needed
                exit = fadeOut(targetAlpha = 0.0f) // Customize as needed
            ) {
                Popup(
                    properties = PopupProperties(focusable = false),
                ) {
                    Text(
                        text = "Copied!",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    )
                }
            }

        }



        FilledTonalIconButton(onClick = onLaunch) {
            Icon(
                painter = painterResource(MR.images.ic_launch_24dp),
                contentDescription = "Launch",
            )
        }
    }
}