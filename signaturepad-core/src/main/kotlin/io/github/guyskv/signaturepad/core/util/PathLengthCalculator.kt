package io.github.guyskv.signaturepad.core.util

import io.github.guyskv.signaturepad.core.model.SignatureStroke
import kotlin.math.sqrt

/**
 * Calculates the total path length of signature strokes.
 *
 * Path length is the sum of Euclidean distances between consecutive points
 * in each stroke. Used for validation to reject signatures that are too short.
 */
object PathLengthCalculator {

    /**
     * Calculates the total path length across all strokes.
     *
     * @param strokes The list of strokes to measure.
     * @return The total path length in pixels. Returns 0 if there are no strokes or points.
     */
    fun calculateTotalPathLength(strokes: List<SignatureStroke>): Float {
        var totalLength = 0f
        for (stroke in strokes) {
            totalLength += calculateStrokeLength(stroke)
        }
        return totalLength
    }

    /**
     * Calculates the path length of a single stroke.
     *
     * @param stroke The stroke to measure.
     * @return The path length in pixels.
     */
    fun calculateStrokeLength(stroke: SignatureStroke): Float {
        val points = stroke.points
        if (points.size < 2) return 0f

        var length = 0f
        for (i in 1 until points.size) {
            val dx = points[i].x - points[i - 1].x
            val dy = points[i].y - points[i - 1].y
            length += sqrt(dx * dx + dy * dy)
        }
        return length
    }
}
