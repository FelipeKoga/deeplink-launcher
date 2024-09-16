package dev.koga.deeplinklauncher.screen.component.targets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.koga.deeplinklauncher.theme.typography
import dev.koga.deeplinklauncher.util.ext.hoverIndication
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun DeeplinkTargetsDropDown(
    modifier: Modifier,
    manager: DeeplinkTargetsDropdownManager = koinInject(),
) {
    val uiState by manager.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded && uiState.targets.size > 1
        },
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .onPointerEvent(PointerEventType.Scroll) {
                val delta = it.changes.first().scrollDelta.y.toInt()
                when {
                    delta < 0 -> manager.prev()
                    delta > 0 -> manager.next()
                }
            }
            .border(
                width = 1.dp,
                color = colorScheme.secondary.copy(alpha = .3f),
                shape = RoundedCornerShape(4.dp),
            )
            .hoverIndication(
                enabled = uiState.targets.size > 1,
            ),
    ) {
        Row(
            modifier = Modifier
                .menuAnchor()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = uiState.selected.icon,
                contentDescription = null,
                tint = colorScheme.secondary,
                modifier = Modifier.size(18.dp),
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = uiState.selected.name,
                style = typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                ),
            )

            Spacer(Modifier.width(8.dp))

            if (uiState.targets.size > 1) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = colorScheme.secondary,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize(
                matchTextFieldWidth = false,
            ),
        ) {
            uiState.targets.forEach { target ->
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = target.icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                    },
                    text = {
                        Text(
                            text = target.name,
                            style = typography.labelLarge.copy(
                                fontWeight = if (target.selected) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.SemiBold
                                },
                            ),
                        )
                    },
                    onClick = {
                        manager.select(target.deeplinkTarget)
                        expanded = false
                    },
                )
            }
        }
    }
}
