package io.github.guyskv.signaturepad.core.model

import kotlinx.serialization.Serializable

/**
 * Platform-neutral color representation for signature strokes.
 *
 * Uses ARGB integer format (0xAARRGGBB). This avoids exposing
 * Compose [Color] from the core module, keeping it platform-independent.
 *
 * @param argb The color value in ARGB format.
 */
@Serializable
data class SignatureColor(
    val argb: Int
) {
    /**
     * Returns the color as a CSS hex string (e.g., "#FF000000").
     */
    fun toHexString(): String {
        return "#${argb.toUInt().toString(16).padStart(8, '0').uppercase()}"
    }

    /**
     * Returns the RGB portion of the color as a standard 6-digit hex string (e.g., "#000000").
     * Safely compatible with all SVG viewers.
     */
    fun toSvgColor(): String {
        val r = (argb ushr 16) and 0xFF
        val g = (argb ushr 8) and 0xFF
        val b = argb and 0xFF
        return "#${r.toString(16).padStart(2, '0')}${g.toString(16).padStart(2, '0')}${b.toString(16).padStart(2, '0')}".uppercase()
    }

    /**
     * Returns the alpha component of the color as a float between 0.0 and 1.0.
     * Safely compatible with SVG stroke-opacity attribute.
     */
    fun toSvgOpacity(): Float {
        val a = (argb ushr 24) and 0xFF
        return a / 255f
    }

    companion object {
        val Black = SignatureColor(0xFF000000.toInt())
        val White = SignatureColor(0xFFFFFFFF.toInt())
        val Transparent = SignatureColor(0x00000000)

        /**
         * Parses a hex color string (e.g., "#FF000000" or "#000000") into a [SignatureColor].
         */
        fun fromHexString(hex: String): SignatureColor {
            val cleanHex = hex.removePrefix("#")
            val argb = when (cleanHex.length) {
                6 -> (0xFF000000 or cleanHex.toLong(16)).toInt()
                8 -> cleanHex.toLong(16).toInt()
                else -> throw IllegalArgumentException("Invalid hex color: $hex")
            }
            return SignatureColor(argb)
        }
    }
}
