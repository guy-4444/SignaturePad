package io.github.guyskv.signaturepad.core.export

import io.github.guyskv.signaturepad.core.model.SignatureColor

/**
 * Options for exporting a signature as a PNG image.
 *
 * @param widthPx Optional fixed width in pixels. If null, uses the signature bounds.
 * @param heightPx Optional fixed height in pixels. If null, uses the signature bounds.
 * @param backgroundColor Optional background color. Ignored if [transparentBackground] is true.
 * @param transparentBackground If true, the PNG background will be transparent.
 * @param trimToSignature If true, the PNG will be cropped to the signature bounds (plus padding).
 * @param paddingPx Padding around the signature when trimming, in pixels.
 * @param scale Scale factor for the output resolution (e.g., 2f = 2x resolution).
 */
data class SignaturePngExportOptions(
    val widthPx: Int? = null,
    val heightPx: Int? = null,
    val backgroundColor: SignatureColor? = null,
    val transparentBackground: Boolean = true,
    val trimToSignature: Boolean = true,
    val paddingPx: Int = 24,
    val scale: Float = 2f
)
