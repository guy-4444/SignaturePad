package io.github.guyskv.signaturepad.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.github.guyskv.signaturepad.core.validation.SignatureValidationConfig

/**
 * Creates and remembers a [SignaturePadState] instance across recompositions.
 *
 * @param validationConfig Configuration for signature validation thresholds.
 * @return A [SignaturePadState] that survives recomposition.
 */
@Composable
fun rememberSignaturePadState(
    validationConfig: SignatureValidationConfig = SignatureValidationConfig()
): SignaturePadState {
    return remember {
        SignaturePadState(validationConfig)
    }
}
