package dev.koga.deeplinklauncher.datatransfer.impl.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun JSONBoxViewer(text: String) {
    val colors = DeepLinkTheme.colors

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colors.surface.card)
            .border(
                width = 1.dp,
                color = colors.border.default,
                shape = RoundedCornerShape(12.dp),
            )
            .horizontalScroll(
                rememberScrollState(),
            ),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(24.dp),
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            ),
        )
    }
}
