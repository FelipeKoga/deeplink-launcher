package dev.koga.deeplinklauncher.screen.component.launchtarget

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.koga.deeplinklauncher.theme.typography
import org.koin.compose.koinInject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchTarget() {

    val manager: LaunchTargetManager = koinInject()

    val uiState by manager.uiState.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded && uiState.targets.size > 1
        }
    ) {

        val interactionSource = remember { MutableInteractionSource() }

        Row(
            modifier = Modifier
                .menuAnchor()
                .clip(RoundedCornerShape(4.dp))
                .indication(
                    indication = LocalIndication.current,
                    interactionSource = interactionSource
                ).hoverable(
                    enabled = uiState.targets.size > 1,
                    interactionSource = interactionSource
                )
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


@Preview
@Composable
private fun LaunchTargetPreview() {
    MaterialTheme {
        LaunchTarget()
    }
}