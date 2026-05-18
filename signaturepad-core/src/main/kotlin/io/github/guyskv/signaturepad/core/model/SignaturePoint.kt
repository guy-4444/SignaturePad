package io.github.guyskv.signaturepad.core.model

import kotlinx.serialization.Serializable

/**
 * Represents a single point captured during signature drawing.
 *
 * @param x The x-coordinate of the point in pixels.
 * @param y The y-coordinate of the point in pixels.
 * @param timestampMillis The timestamp when the point was captured, in milliseconds.
 * @param pressure The optional pressure value from a stylus input (0.0 to 1.0).
 */
@Serializable
data class SignaturePoint(
    val x: Float,
    val y: Float,
    val timestampMillis: Long,
    val pressure: Float? = null
)
