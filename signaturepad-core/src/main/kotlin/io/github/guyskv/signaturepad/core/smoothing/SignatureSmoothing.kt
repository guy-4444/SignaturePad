package io.github.guyskv.signaturepad.core.smoothing

/**
 * Controls the level of path smoothing applied when rendering signature strokes.
 *
 * - [None]: Raw line segments (lineTo). Fast but jagged.
 * - [Low]: Light quadratic Bézier smoothing. Slight curve at midpoints.
 * - [Medium]: Standard smoothing. Good balance of accuracy and visual quality.
 * - [High]: Maximum smoothing. Very smooth curves, may slightly deviate from raw input.
 */
enum class SignatureSmoothing {
    None,
    Low,
    Medium,
    High
}
