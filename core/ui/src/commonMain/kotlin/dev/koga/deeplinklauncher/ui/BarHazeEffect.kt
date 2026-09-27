package dev.koga.deeplinklauncher.ui

import androidx.compose.ui.Modifier
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect

@OptIn(ExperimentalHazeApi::class)
fun Modifier.barHazeEffect(state: HazeState, style: HazeStyle): Modifier =
    hazeEffect(state = state, style = style) {
        inputScale = HazeInputScale.Auto
        noiseFactor = 0f
    }
