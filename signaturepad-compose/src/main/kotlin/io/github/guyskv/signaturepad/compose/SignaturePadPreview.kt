package io.github.guyskv.signaturepad.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.guyskv.signaturepad.core.model.SignatureStroke
import io.github.guyskv.signaturepad.core.smoothing.SignatureSmoothing

/**
 * Configuration for the read-only [SignaturePadPreview] composable.
 *
 * @param backgroundColor The background color of the preview.
 * @param cornerRadius Corner radius for rounding the preview's corners.
 * @param showMiddleGuideline Whether to show a horizontal guideline through the center.
 * @param guidelineColor The color of the middle guideline.
 * @param smoothing The smoothing level for rendering strokes.
 */
data class SignaturePadPreviewConfig(
    val backgroundColor: Color = Color.Transparent,
    val cornerRadius: Dp = 12.dp,
    val showMiddleGuideline: Boolean = false,
    val guidelineColor: Color = Color.LightGray,
    val smoothing: SignatureSmoothing = SignatureSmoothing.Medium
)

/**
 * A read-only composable that displays saved signature strokes without allowing editing.
 *
 * Use this to show a previously captured signature in a form confirmation,
 * review screen, or document display.
 *
 * @param strokes The list of strokes to render.
 * @param modifier Modifier for the preview layout.
 * @param config Configuration for appearance.
 */
@Composable
fun SignaturePadPreview(
    strokes: List<SignatureStroke>,
    modifier: Modifier = Modifier,
    config: SignaturePadPreviewConfig = SignaturePadPreviewConfig()
) {
    val density = LocalDensity.current
    val shape = RoundedCornerShape(config.cornerRadius)

    Box(
        modifier = modifier
            .clip(shape)
            .background(config.backgroundColor, shape)
            .semantics { contentDescription = "Signature preview" }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw middle guideline (behind strokes)
            if (config.showMiddleGuideline) {
                val guidelineStrokeWidthPx = with(density) { 1.dp.toPx() }
                val centerY = size.height / 2f
                drawLine(
                    color = config.guidelineColor,
                    start = Offset(0f, centerY),
                    end = Offset(size.width, centerY),
                    strokeWidth = guidelineStrokeWidthPx
                )
            }

            // Draw strokes
            for (stroke in strokes) {
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
        }
    }
}
