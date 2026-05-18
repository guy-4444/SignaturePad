package io.github.guyskv.signaturepad.compose.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A lightweight circular clear button for the signature pad.
 *
 * Draws an "✕" icon using Canvas (no Material3 dependency).
 * Appears only when the signature is not empty.
 *
 * @param onClick Called when the button is pressed.
 * @param alignment Alignment within the parent Box.
 * @param size The size of the button.
 * @param padding Padding from the edge.
 */
@Composable
internal fun ClearButton(
    onClick: () -> Unit,
    alignment: Alignment = Alignment.TopEnd,
    size: Dp = 32.dp,
    padding: Dp = 8.dp
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .padding(padding)
                .size(size)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(onClick = onClick)
                .semantics { contentDescription = "Clear signature" },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(size * 0.45f)) {
                val strokeWidth = 2.dp.toPx()
                val padding = 0f

                // Draw "✕" icon
                drawLine(
                    color = Color.White,
                    start = Offset(padding, padding),
                    end = Offset(this.size.width - padding, this.size.height - padding),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = Color.White,
                    start = Offset(this.size.width - padding, padding),
                    end = Offset(padding, this.size.height - padding),
                    strokeWidth = strokeWidth
                )
            }
        }
    }
}
