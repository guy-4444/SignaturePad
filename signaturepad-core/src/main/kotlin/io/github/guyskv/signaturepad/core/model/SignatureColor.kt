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
