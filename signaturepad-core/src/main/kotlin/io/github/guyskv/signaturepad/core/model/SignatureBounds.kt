package io.github.guyskv.signaturepad.core.model

/**
 * Represents the axis-aligned bounding box of a signature.
 *
 * Calculated from all points across all strokes. Used for
 * validation (minimum size checks) and export trimming.
 *
 * @param left The minimum x-coordinate.
 * @param top The minimum y-coordinate.
 * @param right The maximum x-coordinate.
 * @param bottom The maximum y-coordinate.
 */
data class SignatureBounds(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) {
    /** The width of the bounding box in pixels. */
    val width: Float get() = right - left

    /** The height of the bounding box in pixels. */
    val height: Float get() = bottom - top
}
