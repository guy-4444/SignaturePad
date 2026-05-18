package io.github.guyskv.signaturepad.core.util

import io.github.guyskv.signaturepad.core.model.SignatureBounds
import io.github.guyskv.signaturepad.core.model.SignatureStroke

/**
 * Calculates the axis-aligned bounding box for a collection of signature strokes.
 */
object BoundsCalculator {

    /**
     * Calculates the bounding box encompassing all points in all strokes.
     *
     * @param strokes The list of strokes to calculate bounds for.
     * @return The [SignatureBounds] covering all points, or `null` if there are no points.
     */
    fun calculateBounds(strokes: List<SignatureStroke>): SignatureBounds? {
        val allPoints = strokes.flatMap { it.points }
        if (allPoints.isEmpty()) return null

        var minX = Float.MAX_VALUE
        var minY = Float.MAX_VALUE
        var maxX = Float.MIN_VALUE
        var maxY = Float.MIN_VALUE

        for (point in allPoints) {
            if (point.x < minX) minX = point.x
            if (point.y < minY) minY = point.y
            if (point.x > maxX) maxX = point.x
            if (point.y > maxY) maxY = point.y
        }

        return SignatureBounds(
            left = minX,
            top = minY,
            right = maxX,
            bottom = maxY
        )
    }
}
