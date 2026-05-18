package io.github.guyskv.signaturepad.compose

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path as AndroidPath
import io.github.guyskv.signaturepad.core.export.SignatureExportException
import io.github.guyskv.signaturepad.core.export.SignaturePngExportOptions
import io.github.guyskv.signaturepad.core.model.SignaturePoint
import io.github.guyskv.signaturepad.core.model.SignatureStroke
import io.github.guyskv.signaturepad.core.smoothing.SignatureSmoothing
import io.github.guyskv.signaturepad.core.util.BoundsCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

/**
 * Renders signature strokes to a PNG byte array using Android's Bitmap and Canvas APIs.
 *
 * This runs on [Dispatchers.Default] since PNG rendering can be CPU-intensive.
 */
internal object PngExporter {

    /**
     * Exports signature strokes as a PNG byte array.
     *
     * @param strokes The strokes to render.
     * @param smoothing The smoothing level for path rendering.
     * @param options PNG export options.
     * @return PNG image data as a ByteArray.
     * @throws SignatureExportException If strokes are empty.
     */
    suspend fun export(
        strokes: List<SignatureStroke>,
        smoothing: SignatureSmoothing,
        options: SignaturePngExportOptions
    ): ByteArray = withContext(Dispatchers.Default) {
        if (strokes.isEmpty()) {
            throw SignatureExportException("Cannot export PNG: signature is empty.")
        }

        val bounds = BoundsCalculator.calculateBounds(strokes)
            ?: throw SignatureExportException("Cannot export PNG: no points found.")

        val scale = options.scale
        val padding = options.paddingPx * scale

        // Calculate dimensions
        val bitmapWidth: Float
        val bitmapHeight: Float
        val offsetX: Float
        val offsetY: Float

        if (options.trimToSignature) {
            bitmapWidth = options.widthPx?.let { it * scale }
                ?: ((bounds.width * scale) + padding * 2)
            bitmapHeight = options.heightPx?.let { it * scale }
                ?: ((bounds.height * scale) + padding * 2)
            offsetX = -bounds.left * scale + padding
            offsetY = -bounds.top * scale + padding
        } else {
            bitmapWidth = options.widthPx?.let { it * scale }
                ?: ((bounds.right + options.paddingPx) * scale)
            bitmapHeight = options.heightPx?.let { it * scale }
                ?: ((bounds.bottom + options.paddingPx) * scale)
            offsetX = 0f
            offsetY = 0f
        }

        val width = bitmapWidth.toInt().coerceAtLeast(1)
        val height = bitmapHeight.toInt().coerceAtLeast(1)

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)

        // Draw background
        if (!options.transparentBackground) {
            val bgColor = options.backgroundColor?.argb ?: 0xFFFFFFFF.toInt()
            canvas.drawColor(bgColor)
        }

        // Draw strokes
        for (stroke in strokes) {
            val paint = Paint().apply {
                color = stroke.color.argb
                strokeWidth = stroke.widthPx * scale
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND
                strokeJoin = Paint.Join.ROUND
                isAntiAlias = true
            }

            val path = strokeToAndroidPath(stroke, smoothing, scale, offsetX, offsetY)
            canvas.drawPath(path, paint)
        }

        // Encode to PNG
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        bitmap.recycle()

        outputStream.toByteArray()
    }

    /**
     * Converts a SignatureStroke to an Android Path with scaling and offset.
     */
    private fun strokeToAndroidPath(
        stroke: SignatureStroke,
        smoothing: SignatureSmoothing,
        scale: Float,
        offsetX: Float,
        offsetY: Float
    ): AndroidPath {
        val path = AndroidPath()
        val points = stroke.points
        if (points.isEmpty()) return path

        fun tx(p: SignaturePoint) = p.x * scale + offsetX
        fun ty(p: SignaturePoint) = p.y * scale + offsetY

        if (points.size == 1) {
            path.moveTo(tx(points[0]), ty(points[0]))
            path.lineTo(tx(points[0]) + 0.1f, ty(points[0]) + 0.1f)
            return path
        }

        when (smoothing) {
            SignatureSmoothing.None -> {
                path.moveTo(tx(points[0]), ty(points[0]))
                for (i in 1 until points.size) {
                    path.lineTo(tx(points[i]), ty(points[i]))
                }
            }

            else -> {
                path.moveTo(tx(points[0]), ty(points[0]))
                if (points.size == 2) {
                    path.lineTo(tx(points[1]), ty(points[1]))
                } else {
                    for (i in 1 until points.size) {
                        val prev = points[i - 1]
                        val curr = points[i]
                        val midX = (tx(prev) + tx(curr)) / 2f
                        val midY = (ty(prev) + ty(curr)) / 2f
                        path.quadTo(tx(prev), ty(prev), midX, midY)
                    }
                    path.lineTo(tx(points.last()), ty(points.last()))
                }
            }
        }

        return path
    }
}
