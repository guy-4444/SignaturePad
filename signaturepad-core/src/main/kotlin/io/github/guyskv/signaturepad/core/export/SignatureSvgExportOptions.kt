package io.github.guyskv.signaturepad.core.export

import io.github.guyskv.signaturepad.core.model.SignatureColor

/**
 * Options for exporting a signature as an SVG string.
 *
 * @param widthPx Optional fixed width in pixels. If null, uses the signature bounds.
 * @param heightPx Optional fixed height in pixels. If null, uses the signature bounds.
 * @param trimToSignature If true, the SVG will be cropped to the signature bounds (plus padding).
 * @param paddingPx Padding around the signature when trimming, in pixels.
 * @param includeBackground If true, a background rectangle will be included in the SVG.
 * @param backgroundColor The background color if [includeBackground] is true.
 */
data class SignatureSvgExportOptions(
    val widthPx: Int? = null,
    val heightPx: Int? = null,
    val trimToSignature: Boolean = true,
    val paddingPx: Int = 24,
    val includeBackground: Boolean = false,
    val backgroundColor: SignatureColor? = null
)
