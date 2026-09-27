package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.ConfigureSheetHostWindow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WindowlessBottomSheet(
    state: WindowlessBottomSheetState,
    onDismissed: () -> Unit,
    containerColor: Color,
    contentReady: Boolean,
    content: @Composable ColumnScope.() -> Unit,
) {
    ConfigureSheetHostWindow()

    val currentOnDismissed by rememberUpdatedState(onDismissed)
    LaunchedEffect(state) {
        snapshotFlow { state.isDismissed }.first { it }
        currentOnDismissed()
    }

    LaunchedEffect(state.height, contentReady) {
        if (contentReady && state.height > 0) state.enter()
    }

    val dismissVelocity = with(LocalDensity.current) { DISMISS_VELOCITY.toPx() }
    val nestedScrollConnection = remember(state, dismissVelocity) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset =
                if (available.y < 0 && state.offset > 0f) {
                    Offset(0f, state.dragBy(available.y))
                } else {
                    Offset.Zero
                }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset = if (source == NestedScrollSource.UserInput && available.y > 0) {
                Offset(0f, state.dragBy(available.y))
            } else {
                Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity =
                if (state.offset > 0f) {
                    state.settle(available.y, dismissVelocity)
                    available
                } else {
                    Velocity.Zero
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = when {
                        !state.entered -> 0f
                        state.height == 0 -> 1f
                        else -> 1f - state.offset / state.height
                    }
                }
                .background(Color.Black.copy(alpha = SCRIM_ALPHA))
                .then(
                    if (state.entered) {
                        Modifier
                            .pointerInput(state) { detectTapGestures { state.dismiss() } }
                            .semantics {
                                contentDescription = "Close sheet"
                                onClick {
                                    state.dismiss()
                                    true
                                }
                            }
                    } else {
                        Modifier
                    },
                ),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .statusBarsPadding()
                .widthIn(max = SHEET_MAX_WIDTH)
                .fillMaxWidth()
                .onSizeChanged { state.height = it.height }
                .offset { IntOffset(0, state.offset.roundToInt()) }
                .graphicsLayer { alpha = if (state.entered) 1f else 0f }
                .blockPointerInput(blocked = !state.entered)
                .clip(BottomSheetDefaults.ExpandedShape)
                .background(containerColor)
                .nestedScroll(nestedScrollConnection)
                .draggable(
                    state = rememberDraggableState { delta -> state.dragBy(delta) },
                    orientation = Orientation.Vertical,
                    enabled = state.entered,
                    onDragStopped = { velocity -> state.settle(velocity, dismissVelocity) },
                ),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .semantics(mergeDescendants = true) {
                        dismiss {
                            state.dismiss()
                            true
                        }
                    },
            ) {
                BottomSheetDefaults.DragHandle()
            }
            content()
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

@Composable
internal fun rememberWindowlessBottomSheetState(): WindowlessBottomSheetState {
    val scope = rememberCoroutineScope()
    return rememberSaveable(
        saver = Saver<WindowlessBottomSheetState, Boolean>(
            save = { it.entered },
            restore = { WindowlessBottomSheetState(scope, entered = it) },
        ),
    ) {
        WindowlessBottomSheetState(scope, entered = false)
    }
}

@Stable
internal class WindowlessBottomSheetState(
    private val scope: CoroutineScope,
    entered: Boolean,
) {
    var offset by mutableFloatStateOf(0f)
        private set
    var height by mutableIntStateOf(0)
    var entered by mutableStateOf(entered)
        private set
    var isDismissed by mutableStateOf(false)
        private set

    private var dismissing = false
    private var animation: Job? = null

    fun dismiss() {
        if (dismissing) return
        dismissing = true
        if (!entered || height == 0) {
            isDismissed = true
        } else {
            animateTo(height.toFloat()) { isDismissed = true }
        }
    }

    fun enter() {
        if (entered || dismissing) return
        offset = height.toFloat()
        entered = true
        animateTo(0f)
    }

    fun dragBy(delta: Float): Float {
        if (dismissing) return 0f
        animation?.cancel()
        val newOffset = (offset + delta).coerceIn(0f, height.toFloat())
        val consumed = newOffset - offset
        offset = newOffset
        return consumed
    }

    fun settle(velocity: Float, dismissVelocity: Float) {
        if (dismissing) return
        if (offset > height * DISMISS_FRACTION || velocity > dismissVelocity) {
            dismiss()
        } else {
            animateTo(0f)
        }
    }

    private fun animateTo(target: Float, onEnd: () -> Unit = {}) {
        animation?.cancel()
        animation = scope.launch {
            animate(
                initialValue = offset,
                targetValue = target,
                animationSpec = tween(ANIMATION_DURATION_MS, easing = FastOutSlowInEasing),
            ) { value, _ -> offset = value }
            onEnd()
        }
    }
}

private fun Modifier.blockPointerInput(blocked: Boolean): Modifier = pointerInput(blocked) {
    if (!blocked) return@pointerInput
    awaitPointerEventScope {
        while (true) {
            awaitPointerEvent(PointerEventPass.Initial).changes.forEach { it.consume() }
        }
    }
}

private const val ANIMATION_DURATION_MS = 250
private const val DISMISS_FRACTION = 0.4f
private const val SCRIM_ALPHA = 0.66f
private val DISMISS_VELOCITY = 500.dp
private val SHEET_MAX_WIDTH = 640.dp
