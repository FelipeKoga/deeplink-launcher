package dev.koga.deeplinklauncher.home.ui.util.ext

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Modifier.hoverIndication(
    interactionSource: MutableInteractionSource = remember {
        MutableInteractionSource()
    },
    enabled: Boolean = true,
    indication: Indication = LocalIndication.current,
): Modifier {
    return indication(
        indication = indication,
        interactionSource = interactionSource,
    ).hoverable(
        enabled = enabled,
        interactionSource = interactionSource,
    )
}
