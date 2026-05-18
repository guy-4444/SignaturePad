package io.github.guyskv.signaturepad.compose

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.github.guyskv.signaturepad.core.model.SignatureColor

/**
 * Converts a Compose [Color] to a core [SignatureColor].
 */
fun Color.toSignatureColor(): SignatureColor {
    return SignatureColor(this.toArgb())
}

/**
 * Converts a core [SignatureColor] to a Compose [Color].
 */
fun SignatureColor.toComposeColor(): Color {
    return Color(this.argb)
}
