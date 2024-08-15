package dev.koga.deeplinklauncher.screen.component.launchtarget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun LaunchTarget() {

    val manager: LaunchTargetManager = koinInject()

    val uiState by manager.uiState.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded && uiState.targets.size > 1
        },
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .onPointerEvent(PointerEventType.Scroll) {

                val delta = it.changes.first().scrollDelta.y.toInt()

                when {
                    delta < 0 -> manager.prev()
                    delta > 0 -> manager.next()
                }
            }
            .hoverIndication(
                enabled = uiState.targets.size > 1,
            )
    ) {

        Row(
            modifier = Modifier
                .menuAnchor()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = uiState.selected.icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = uiState.selected.name,
                style = typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )

            if (uiState.targets.size > 1) {

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(18.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize(
                matchTextFieldWidth = false
            )
        ) {
            uiState.targets.forEach { target ->
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = target.icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
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
                                }
                            )
                        )
                    },
                    onClick = {
                        manager.select(target.name)
                        expanded = false
                    },
                )
            }
        }
    }
}