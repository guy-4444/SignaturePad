package io.github.guyskv.signaturepad.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import io.github.guyskv.signaturepad.compose.controls.ClearButton
import io.github.guyskv.signaturepad.compose.controls.DoneButton
import io.github.guyskv.signaturepad.core.model.SignaturePoint
import io.github.guyskv.signaturepad.core.model.SignatureStroke

/**
 * A composable signature pad that allows users to draw handwritten signatures.
 *
 * The pad captures finger/stylus input, renders smooth strokes, and provides
 * optional built-in clear and done buttons. It supports customizable appearance,
 * path smoothing, middle guideline, and corner radius clipping.
 *
 * @param state The [SignaturePadState] managing strokes, undo/redo, and exports.
 * @param modifier Modifier for the signature pad layout.
 * @param config Configuration for appearance, behavior, and controls.
 * @param onStrokeStarted Called when the user starts drawing a new stroke.
 * @param onStrokeFinished Called when the user finishes drawing a stroke.
 * @param onSignatureChanged Called after any mutation (stroke added, clear, undo, redo, import).
 * @param onDone Called when the done button is pressed.
 */
@Composable
fun SignaturePad(
    state: SignaturePadState,
    modifier: Modifier = Modifier,
    config: SignaturePadConfig = SignaturePadConfig(),
    onStrokeStarted: (() -> Unit)? = null,
    onStrokeFinished: (() -> Unit)? = null,
    onSignatureChanged: (() -> Unit)? = null,
    onDone: (() -> Unit)? = null
) {
    val density = LocalDensity.current
    val strokeWidthPx = with(density) { config.strokeWidth.toPx() }
    val guidelineStrokeWidthPx = with(density) { config.guidelineStrokeWidth.toPx() }

    // Set the smoothing level on the state so export functions can use it
    LaunchedEffect(config.smoothing) {
        state.smoothing = config.smoothing
    }

    val strokeColor = config.strokeColor
    val strokeColorForCore = strokeColor.toSignatureColor()

    // Update existing strokes if config changes
    LaunchedEffect(strokeColorForCore, strokeWidthPx) {
        state.updateStrokeProperties(strokeColorForCore, strokeWidthPx)
    }

    // Set the onSignatureChanged callback
    LaunchedEffect(onSignatureChanged) {
        state.onSignatureChanged = onSignatureChanged
    }

    // Current stroke being drawn (live points)
    val currentPoints = remember { mutableStateListOf<SignaturePoint>() }
    var isDrawing by remember { mutableStateOf(false) }

    val shape = RoundedCornerShape(config.cornerRadius)

    Box(
        modifier = modifier
            .then(
                if (config.clipToBounds) Modifier.clip(shape)
                else Modifier
            )
            .background(config.backgroundColor, shape)
            .semantics { contentDescription = "Signature pad" }
            .then(
                if (config.enabled) {
                    Modifier.pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                // Wait for first pointer down
                                val down = awaitPointerEvent()
                                val change = down.changes.firstOrNull() ?: continue

                                if (change.pressed) {
                                    // Start a new stroke
                                    isDrawing = true
                                    currentPoints.clear()
                                    currentPoints.add(
                                        SignaturePoint(
                                            x = change.position.x,
                                            y = change.position.y,
                                            timestampMillis = change.uptimeMillis,
                                            pressure = change.pressure.takeIf { it > 0f }
                                        )
                                    )
                                    change.consume()
                                    onStrokeStarted?.invoke()

                                    // Track pointer movement
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        val moveChange = event.changes.firstOrNull() ?: break

                                        if (moveChange.pressed) {
                                            currentPoints.add(
                                                SignaturePoint(
                                                    x = moveChange.position.x,
                                                    y = moveChange.position.y,
                                                    timestampMillis = moveChange.uptimeMillis,
                                                    pressure = moveChange.pressure.takeIf { it > 0f }
                                                )
                                            )
                                            moveChange.consume()
                                        } else {
                                            // Pointer up: finalize the stroke
                                            moveChange.consume()
                                            break
                                        }
                                    }

                                    // Finalize the stroke (ignore accidental strokes with < 2 points)
                                    if (currentPoints.size >= 2) {
                                        val stroke = SignatureStroke(
                                            points = currentPoints.toList(),
                                            color = strokeColorForCore,
                                            widthPx = strokeWidthPx
                                        )
                                        state.addStroke(stroke)
                                        onStrokeFinished?.invoke()
                                    }
                                    currentPoints.clear()
                                    isDrawing = false
                                }
                            }
                        }
                    }
                } else {
                    Modifier
                }
            )
    ) {
        // Canvas for drawing guideline, strokes, and current live stroke
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw middle guideline (behind strokes)
            if (config.showMiddleGuideline) {
                val centerY = size.height / 2f
                drawLine(
                    color = config.guidelineColor,
                    start = Offset(0f, centerY),
                    end = Offset(size.width, centerY),
                    strokeWidth = guidelineStrokeWidthPx
                )
            }

            // Draw completed strokes
            for (stroke in state.strokes) {
                val path = stroke.toComposePath(config.smoothing)
                drawPath(
                    path = path,
                    color = stroke.color.toComposeColor(),
                    style = Stroke(
                        width = stroke.widthPx,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // Draw the current stroke being drawn (live)
            if (isDrawing && currentPoints.size >= 2) {
                val liveStroke = SignatureStroke(
                    points = currentPoints.toList(),
                    color = strokeColorForCore,
                    widthPx = strokeWidthPx
                )
                val livePath = liveStroke.toComposePath(config.smoothing)
                drawPath(
                    path = livePath,
                    color = strokeColor,
                    style = Stroke(
                        width = strokeWidthPx,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }

        // Clear button — shown only when pad is not empty
        if (config.showClearButton && !state.isEmpty) {
            ClearButton(
                onClick = { state.clear() },
                alignment = config.clearButtonAlignment,
                size = config.clearButtonSize,
                padding = config.clearButtonPadding
            )
        }

        // Done button — shown only when configured, enabled only with valid signature
        if (config.showDoneButton) {
            DoneButton(
                onClick = { onDone?.invoke() },
                enabled = state.hasValidSignature,
                alignment = config.doneButtonAlignment,
                size = config.doneButtonSize,
                padding = config.doneButtonPadding,
                cornerRadius = config.cornerRadius
            )
        }
    }
}
