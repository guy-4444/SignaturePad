package io.github.guyskv.signaturepad.core.validation

/**
 * Configuration for signature validation thresholds.
 *
 * A signature is considered valid only when ALL conditions are met.
 * This prevents accidental taps, tiny dots, or minimal strokes from
 * being accepted as legitimate signatures.
 *
 * The defaults are intentionally lenient to allow short initials while
 * rejecting clearly accidental input.
 *
 * @param minStrokeCount Minimum number of strokes required.
 * @param minTotalPointCount Minimum total number of points across all strokes.
 * @param minBoundingBoxWidthPx Minimum width of the signature bounding box in pixels.
 * @param minBoundingBoxHeightPx Minimum height of the signature bounding box in pixels.
 * @param minTotalPathLengthPx Minimum total path length across all strokes in pixels.
 */
data class SignatureValidationConfig(
    val minStrokeCount: Int = 1,
    val minTotalPointCount: Int = 8,
    val minBoundingBoxWidthPx: Float = 48f,
    val minBoundingBoxHeightPx: Float = 16f,
    val minTotalPathLengthPx: Float = 120f
)
