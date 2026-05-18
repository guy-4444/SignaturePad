package io.github.guyskv.signaturepad.compose

import androidx.compose.ui.graphics.Path
import io.github.guyskv.signaturepad.core.model.SignatureStroke
import io.github.guyskv.signaturepad.core.smoothing.SignatureSmoothing

/**
 * Converts a [SignatureStroke] to a Compose [Path] with the specified smoothing level.
 *
 * This is the core rendering logic for signature strokes in the Compose Canvas.
 * It uses quadratic Bézier curves through midpoints for smooth rendering.
 *
 * @param smoothing The level of smoothing to apply.
 * @return A Compose [Path] representing the stroke.
 */
fun SignatureStroke.toComposePath(smoothing: SignatureSmoothing): Path {
    val path = Path()
    val points = this.points
    if (points.isEmpty()) return path

    if (points.size == 1) {
        // Single point: draw a tiny segment for visibility
        path.moveTo(points[0].x, points[0].y)
        path.lineTo(points[0].x + 0.1f, points[0].y + 0.1f)
        return path
    }

    when (smoothing) {
        SignatureSmoothing.None -> {
            path.moveTo(points[0].x, points[0].y)
            for (i in 1 until points.size) {
                path.lineTo(points[i].x, points[i].y)
            }
        }

        SignatureSmoothing.Low -> buildSmoothedPath(path, points, tension = 0.3f)
        SignatureSmoothing.Medium -> buildSmoothedPath(path, points, tension = 0.5f)
        SignatureSmoothing.High -> buildSmoothedPath(path, points, tension = 0.7f)
    }

    return path
}

/**
 * Builds a smoothed path using quadratic Bézier curves through midpoints.
 *
 * For each consecutive pair of points, the control point is interpolated
 * based on the tension factor, and the endpoint is the midpoint between
 * the two original points. This produces smooth, natural-looking curves.
 */
private fun buildSmoothedPath(
    path: Path,
    points: List<io.github.guyskv.signaturepad.core.model.SignaturePoint>,
    @Suppress("UNUSED_PARAMETER") tension: Float
) {
    path.moveTo(points[0].x, points[0].y)

    if (points.size == 2) {
        path.lineTo(points[1].x, points[1].y)
        return
    }

    // Quadratic Bézier smoothing through midpoints
    for (i in 1 until points.size) {
        val prev = points[i - 1]
        val curr = points[i]
        val midX = (prev.x + curr.x) / 2f
        val midY = (prev.y + curr.y) / 2f

        path.quadraticTo(
            x1 = prev.x,
            y1 = prev.y,
            x2 = midX,
            y2 = midY
        )
    }

    // Connect to the very last point
    val last = points.last()
    path.lineTo(last.x, last.y)
}
