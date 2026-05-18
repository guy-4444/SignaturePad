package io.github.guyskv.signaturepad.core.validation

import io.github.guyskv.signaturepad.core.model.SignatureStroke
import io.github.guyskv.signaturepad.core.util.BoundsCalculator
import io.github.guyskv.signaturepad.core.util.PathLengthCalculator

/**
 * Validates whether a collection of strokes constitutes a meaningful signature.
 *
 * Validation checks all five conditions:
 * 1. Stroke count >= [SignatureValidationConfig.minStrokeCount]
 * 2. Total point count >= [SignatureValidationConfig.minTotalPointCount]
 * 3. Bounding box width >= [SignatureValidationConfig.minBoundingBoxWidthPx]
 * 4. Bounding box height >= [SignatureValidationConfig.minBoundingBoxHeightPx]
 * 5. Total path length >= [SignatureValidationConfig.minTotalPathLengthPx]
 *
 * All conditions must be satisfied for the signature to be valid.
 */
object SignatureValidator {

    /**
     * Validates the given strokes against the provided configuration.
     *
     * @param strokes The list of strokes to validate.
     * @param config The validation configuration with threshold values.
     * @return `true` if the signature meets all validation criteria, `false` otherwise.
     */
    fun validate(strokes: List<SignatureStroke>, config: SignatureValidationConfig): Boolean {
        if (strokes.isEmpty()) return false

        // Check 1: Minimum stroke count
        if (strokes.size < config.minStrokeCount) return false

        // Check 2: Minimum total point count
        val totalPoints = strokes.sumOf { it.points.size }
        if (totalPoints < config.minTotalPointCount) return false

        // Check 3 & 4: Minimum bounding box dimensions
        val bounds = BoundsCalculator.calculateBounds(strokes) ?: return false
        if (bounds.width < config.minBoundingBoxWidthPx) return false
        if (bounds.height < config.minBoundingBoxHeightPx) return false

        // Check 5: Minimum total path length
        val totalLength = PathLengthCalculator.calculateTotalPathLength(strokes)
        if (totalLength < config.minTotalPathLengthPx) return false

        return true
    }
}
