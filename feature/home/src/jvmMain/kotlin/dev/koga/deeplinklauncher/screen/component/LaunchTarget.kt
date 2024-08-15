package dev.koga.deeplinklauncher.screen.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.koga.deeplinklauncher.model.Adb
import dev.koga.deeplinklauncher.theme.typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

fun MutableList<Target.Device>.addOrUpdate(device: Target.Device) {
    val index = indexOfFirst { it.name == device.name }

    if (index == -1) {
        add(device)
    } else {
        set(index, device)
    }
}

object DeviceParser {

    operator fun invoke(input: String): Target.Device {

        val parts = input.dropWhile {
            it.isDigit()
        }.split(Regex("\\s+"))

        return Target.Device(
            name = parts[0],
            type = parts[1]
        )
    }
}

class DevicesDataSource(
    private val adb: Adb
) {

    fun getDevices() = flow {

        val devices = mutableListOf<Target.Device>()

        adb.trackDevices()
            .inputStream
            .bufferedReader()
            .useLines {
                it.forEach { line ->
                    devices.addOrUpdate(
                        DeviceParser(line),
                    )

                    emit(devices.filter(Target.Device::isActive))
                }
            }
    }.flowOn(Dispatchers.IO)
}

class LaunchTargetManager(
    private val devicesDataSource: DevicesDataSource
) {

    private val coroutines = CoroutineScope(Dispatchers.IO)

    private val _targets = MutableStateFlow(listOf<Target>(Target.Browser))
    private val _target = MutableStateFlow<Target>(Target.Browser)

    val target = _target.asStateFlow()
    val targets = _targets.asStateFlow()

    init {
        coroutines.launch {
            devicesDataSource.getDevices().collect { devices ->
                _targets.value = listOf(Target.Browser) + devices
            }
        }

        targets.onEach {
            validateTarget(it)
        }.launchIn(coroutines)
    }

    private fun validateTarget(targets: List<Target>) {
        _target.update { target ->
            targets.find {
                it.name == target.name
            } ?: Target.Browser
        }
    }

    fun select(device: Target) {
        _target.value = device
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchTarget() {

    val manager: LaunchTargetManager = koinInject()

    val targets by manager.targets.collectAsState()

    val selected by manager.target.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded && targets.size > 1
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
                    enabled = targets.size > 1,
                    interactionSource = interactionSource
                )
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = selected.icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = selected.name,
                style = typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )

            if (targets.size > 1) {

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
            targets.forEach { target ->
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
                                fontWeight = if (target == selected) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.SemiBold
                                }
                            )
                        )
                    },
                    onClick = {
                        manager.select(target)
                        expanded = false
                    },
                )
            }
        }
    }
}

sealed class Target {

    abstract val name: String
    abstract val icon: ImageVector

    data object Browser : Target() {
        override val name = "browser"
        override val icon = Icons.Rounded.Public
    }

    data class Device(
        override val name: String,
        val type: String
    ) : Target() {
        override val icon = Icons.Rounded.Devices

        val isActive = type == "device"
    }
}

@Preview
@Composable
private fun LaunchTargetPreview() {
    MaterialTheme {
        LaunchTarget()
    }
}