package io.github.guyskv.signaturepad.compose.controls

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A lightweight done button for the signature pad.
 *
 * Draws a "✓" checkmark icon using Canvas (no Material3 dependency).
 * Visible only when [showDoneButton] is configured. Enabled only when
 * the signature passes validation ([enabled] = true).
 *
 * @param onClick Called when the button is pressed.
 * @param enabled Whether the button is interactive.
 * @param alignment Alignment within the parent Box.
 * @param size The size of the button.
 * @param padding Padding from the edge.
 */
@Composable
internal fun DoneButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    alignment: Alignment = Alignment.BottomEnd,
    size: Dp = 44.dp,
    padding: Dp = 8.dp
) {
    val containerColor = if (enabled) Color(0xFF2E7D32) else Color(0xFF9E9E9E)
    val contentColor = if (enabled) Color.White else Color.White.copy(alpha = 0.5f)
    val semanticLabel = if (enabled) "Done signature" else "Done signature, disabled"

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .padding(padding)
                .size(size)
                .clip(RoundedCornerShape(12.dp))
                .background(containerColor)
                .alpha(if (enabled) 1f else 0.6f)
                .then(
                    if (enabled) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    }
                )
                .semantics { contentDescription = semanticLabel },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(size * 0.45f)) {
                val strokeWidth = 2.5.dp.toPx()

                // Draw "✓" checkmark icon
                val startX = this.size.width * 0.15f
                val startY = this.size.height * 0.55f
                val midX = this.size.width * 0.4f
                val midY = this.size.height * 0.85f
                val endX = this.size.width * 0.85f
                val endY = this.size.height * 0.2f

                drawLine(
                    color = contentColor,
                    start = Offset(startX, startY),
                    end = Offset(midX, midY),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = contentColor,
                    start = Offset(midX, midY),
                    end = Offset(endX, endY),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}
