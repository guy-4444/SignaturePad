package io.github.guyskv.signaturepad.compose

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.guyskv.signaturepad.core.smoothing.SignatureSmoothing

/**
 * Configuration for a [SignaturePad] composable.
 *
 * Controls visual appearance, behavior, and built-in controls.
 *
 * @param strokeColor The color of the signature strokes.
 * @param strokeWidth The width of the signature strokes.
 * @param backgroundColor The background color of the pad.
 * @param enabled Whether the pad accepts touch input. When false, existing strokes are still rendered.
 * @param cornerRadius Corner radius for rounding the pad's corners.
 * @param showClearButton Whether to show the built-in clear button.
 * @param clearButtonAlignment Alignment of the clear button within the pad.
 * @param clearButtonSize The size of the clear button.
 * @param clearButtonPadding Padding around the clear button.
 * @param showDoneButton Whether to show the built-in done button.
 * @param doneButtonAlignment Alignment of the done button within the pad.
 * @param doneButtonSize The size of the done button.
 * @param doneButtonPadding Padding around the done button.
 * @param showMiddleGuideline Whether to show a horizontal guideline through the vertical center.
 * @param guidelineColor The color of the middle guideline.
 * @param guidelineStrokeWidth The stroke width of the middle guideline.
 * @param smoothing The level of path smoothing for stroke rendering.
 * @param clipToBounds Whether to clip drawing to the pad's rounded bounds.
 */
data class SignaturePadConfig(
    val strokeColor: Color = Color.Black,
    val strokeWidth: Dp = 3.dp,
    val backgroundColor: Color = Color.Transparent,

    val enabled: Boolean = true,
    val cornerRadius: Dp = 12.dp,

    val showClearButton: Boolean = true,
    val clearButtonAlignment: Alignment = Alignment.TopEnd,
    val clearButtonSize: Dp = 32.dp,
    val clearButtonPadding: Dp = 8.dp,

    val showDoneButton: Boolean = false,
    val doneButtonAlignment: Alignment = Alignment.BottomEnd,
    val doneButtonSize: Dp = 44.dp,
    val doneButtonPadding: Dp = 8.dp,

    val showMiddleGuideline: Boolean = false,
    val guidelineColor: Color = Color.LightGray,
    val guidelineStrokeWidth: Dp = 1.dp,

    val smoothing: SignatureSmoothing = SignatureSmoothing.Medium,
    val clipToBounds: Boolean = true
)
