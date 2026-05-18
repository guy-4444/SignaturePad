package io.github.guyskv.signaturepad.core.smoothing

import io.github.guyskv.signaturepad.core.model.SignaturePoint
import io.github.guyskv.signaturepad.core.model.SignatureStroke

/**
 * Generates SVG path data strings from signature strokes with optional smoothing.
 *
 * This is used by the SVG exporter and can also serve as a reference
 * for how the Compose renderer should build its [Path] objects.
 */
object PathDataGenerator {

    /**
     * Converts a [SignatureStroke] to an SVG path data string.
     *
     * @param stroke The stroke to convert.
     * @param smoothing The level of smoothing to apply.
     * @return An SVG path data string (e.g., "M 10 20 Q 15 25 20 30 ...").
     */
    fun generatePathData(stroke: SignatureStroke, smoothing: SignatureSmoothing): String {
        val points = stroke.points
        if (points.isEmpty()) return ""
        if (points.size == 1) {
            // Single point: draw a tiny circle-like mark
            val p = points[0]
            return "M ${fmt(p.x)} ${fmt(p.y)} L ${fmt(p.x + 0.1f)} ${fmt(p.y + 0.1f)}"
        }

        return when (smoothing) {
            SignatureSmoothing.None -> generateLinearPath(points)
            SignatureSmoothing.Low -> generateSmoothedPath(points, tension = 0.3f)
            SignatureSmoothing.Medium -> generateSmoothedPath(points, tension = 0.5f)
            SignatureSmoothing.High -> generateSmoothedPath(points, tension = 0.7f)
        }
    }

    private fun generateLinearPath(points: List<SignaturePoint>): String {
        val sb = StringBuilder()
        sb.append("M ${fmt(points[0].x)} ${fmt(points[0].y)}")
        for (i in 1 until points.size) {
            sb.append(" L ${fmt(points[i].x)} ${fmt(points[i].y)}")
        }
        return sb.toString()
    }

    private fun generateSmoothedPath(points: List<SignaturePoint>, tension: Float): String {
        val sb = StringBuilder()
        sb.append("M ${fmt(points[0].x)} ${fmt(points[0].y)}")

        if (points.size == 2) {
            sb.append(" L ${fmt(points[1].x)} ${fmt(points[1].y)}")
            return sb.toString()
        }

        // Quadratic Bézier smoothing through midpoints
        for (i in 1 until points.size) {
            val prev = points[i - 1]
            val curr = points[i]
            val midX = (prev.x + curr.x) / 2f
            val midY = (prev.y + curr.y) / 2f

            // Control point is the previous point, end point is the midpoint
            // The tension factor blends between linear and fully smoothed
            val cpX = prev.x + (curr.x - prev.x) * tension
            val cpY = prev.y + (curr.y - prev.y) * tension

            sb.append(" Q ${fmt(cpX)} ${fmt(cpY)} ${fmt(midX)} ${fmt(midY)}")
        }

        // Connect to the last point
        val last = points.last()
        sb.append(" L ${fmt(last.x)} ${fmt(last.y)}")

        return sb.toString()
    }

    private fun fmt(value: Float): String {
        return "%.2f".format(value)
    }
}
