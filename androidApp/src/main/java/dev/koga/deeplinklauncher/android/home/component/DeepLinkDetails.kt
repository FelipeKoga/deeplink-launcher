package dev.koga.deeplinklauncher.android.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.android.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeepLinkDetailsBottomSheet(
    modifier: Modifier = Modifier,
    deepLink: String,
    onDismiss: () -> Unit,
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        DetailsContent(deepLink)
    }
}

@Composable
private fun DetailsContent(deepLink: String) {
    Column(Modifier.padding(24.dp)) {


        Text(
            text = deepLink,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Divider()

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            FilledTonalIconButton(
                onClick = {
                },
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = Color.Red.copy(alpha = .2f)
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "",
                    modifier = Modifier.size(18.dp),
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = {
            }) {
                Icon(
                    imageVector = Icons.Rounded.Share,
                    contentDescription = "",
                    modifier = Modifier.size(18.dp),
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            IconButton(onClick = {
            }) {
                Icon(
                    imageVector = Icons.Rounded.FavoriteBorder,
                    contentDescription = "",
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        Spacer(modifier = Modifier.statusBarsPadding())
    }

}

@Preview(showBackground = true)
@Composable
fun DetailsContentPreview() {
    AppTheme {
        DetailsContent(deepLink = "https://www.google.com.br")
    }
}