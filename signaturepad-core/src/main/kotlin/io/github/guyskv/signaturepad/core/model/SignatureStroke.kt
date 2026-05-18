package io.github.guyskv.signaturepad.core.model

import kotlinx.serialization.Serializable

/**
 * Represents a single continuous stroke drawn by the user.
 *
 * A stroke is a sequence of [SignaturePoint]s captured between
 * a pointer-down and pointer-up event, along with the visual
 * properties (color and width) used to render it.
 *
 * @param points The ordered list of points in this stroke.
 * @param color The color of the stroke.
 * @param widthPx The stroke width in pixels.
 */
@Serializable
data class SignatureStroke(
    val points: List<SignaturePoint>,
    val color: SignatureColor,
    val widthPx: Float
)
