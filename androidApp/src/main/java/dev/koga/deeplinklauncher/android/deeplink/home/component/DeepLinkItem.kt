package dev.koga.deeplinklauncher.android.deeplink.home.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.android.R
import dev.koga.deeplinklauncher.model.DeepLink
import kotlinx.datetime.Clock

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeepLinkItem(
    modifier: Modifier = Modifier,
    deepLink: DeepLink,
    onClick: (DeepLink) -> Unit,
    onLaunch: (DeepLink) -> Unit,
    onLongClick: (DeepLink) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick(deepLink) },
                onLongClick = { onLongClick(deepLink) },
            )
    ) {

        Column(
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 12.dp)
                .fillMaxWidth()
        ) {

            deepLink.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {


                Text(
                    text = deepLink.link,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                IconButton(onClick = { onLaunch(deepLink) }) {
                    Icon(
                        painterResource(id = R.drawable.ic_round_launch_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

            }

            deepLink.description?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }

            deepLink.folder?.let {
                Spacer(modifier = Modifier.height(8.dp))
                ElevatedAssistChip(onClick = { /*TODO*/ }, label = {
                    Text(text = it.name)
                })
            }
        }


        HorizontalDivider(
            modifier = Modifier.background(
                MaterialTheme.colorScheme.onSurface.copy(
                    0.1f
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeepLinkItemPreview() {
    DeepLinkItem(
        deepLink = DeepLink(
            id = "123",
            link = "https://www.google.com",
            name = null,
            description = null,
            createdAt = Clock.System.now(),
            isFavorite = false,
            folder = null
        ),
        onClick = {},
        modifier = Modifier, onLaunch = {}, onLongClick = {})
}