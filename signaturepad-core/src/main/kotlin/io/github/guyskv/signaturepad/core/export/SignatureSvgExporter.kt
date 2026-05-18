package io.github.guyskv.signaturepad.core.export

import io.github.guyskv.signaturepad.core.model.SignatureStroke
import io.github.guyskv.signaturepad.core.smoothing.PathDataGenerator
import io.github.guyskv.signaturepad.core.smoothing.SignatureSmoothing
import io.github.guyskv.signaturepad.core.util.BoundsCalculator

/**
 * Exports signature strokes as a valid SVG string.
 */
object SignatureSvgExporter {

    /**
     * Exports the given strokes as an SVG string.
     *
     * @param strokes The strokes to export.
     * @param smoothing The smoothing level for path rendering.
     * @param options Export options for sizing, trimming, and background.
     * @return A valid SVG string.
     * @throws SignatureExportException If there are no strokes to export.
     */
    fun export(
        strokes: List<SignatureStroke>,
        smoothing: SignatureSmoothing = SignatureSmoothing.Medium,
        options: SignatureSvgExportOptions = SignatureSvgExportOptions()
    ): String {
        if (strokes.isEmpty()) {
            throw SignatureExportException("Cannot export SVG: signature is empty.")
        }

        val bounds = BoundsCalculator.calculateBounds(strokes)
            ?: throw SignatureExportException("Cannot export SVG: no points found.")

        val padding = options.paddingPx.toFloat()

        // Determine the viewport
        val viewLeft: Float
        val viewTop: Float
        val viewWidth: Float
        val viewHeight: Float

        if (options.trimToSignature) {
            viewLeft = bounds.left - padding
            viewTop = bounds.top - padding
            viewWidth = bounds.width + padding * 2
            viewHeight = bounds.height + padding * 2
        } else {
            viewLeft = 0f
            viewTop = 0f
            viewWidth = (options.widthPx?.toFloat() ?: (bounds.right + padding))
            viewHeight = (options.heightPx?.toFloat() ?: (bounds.bottom + padding))
        }

        val svgWidth = options.widthPx ?: viewWidth.toInt()
        val svgHeight = options.heightPx ?: viewHeight.toInt()

        val sb = StringBuilder()
        sb.appendLine("""<?xml version="1.0" encoding="UTF-8"?>""")
        sb.appendLine(
            """<svg xmlns="http://www.w3.org/2000/svg" """ +
                """width="$svgWidth" height="$svgHeight" """ +
                """viewBox="${fmt(viewLeft)} ${fmt(viewTop)} ${fmt(viewWidth)} ${fmt(viewHeight)}">"""
        )

        // Optional background rectangle
        if (options.includeBackground) {
            val bgColor = options.backgroundColor?.toHexString() ?: "#FFFFFFFF"
            sb.appendLine(
                """  <rect x="${fmt(viewLeft)}" y="${fmt(viewTop)}" """ +
                    """width="${fmt(viewWidth)}" height="${fmt(viewHeight)}" fill="$bgColor"/>"""
            )
        }

        // Draw each stroke as a path
        for (stroke in strokes) {
            if (stroke.points.isEmpty()) continue

            val pathData = PathDataGenerator.generatePathData(stroke, smoothing)
            val strokeColor = stroke.color.toHexString()
            val strokeWidth = "%.2f".format(stroke.widthPx)

            sb.appendLine(
                """  <path d="$pathData" """ +
                    """fill="none" stroke="$strokeColor" """ +
                    """stroke-width="$strokeWidth" """ +
                    """stroke-linecap="round" stroke-linejoin="round"/>"""
            )
        }

        sb.appendLine("</svg>")
        return sb.toString()
    }

    private fun fmt(value: Float): String = "%.2f".format(value)
}
