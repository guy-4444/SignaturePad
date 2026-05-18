package io.github.guyskv.signaturepad.compose

import androidx.compose.runtime.mutableStateListOf
import io.github.guyskv.signaturepad.core.export.SignatureExportException
import io.github.guyskv.signaturepad.core.export.SignatureJsonExporter
import io.github.guyskv.signaturepad.core.export.SignaturePngExportOptions
import io.github.guyskv.signaturepad.core.export.SignatureSvgExportOptions
import io.github.guyskv.signaturepad.core.export.SignatureSvgExporter
import io.github.guyskv.signaturepad.core.model.SignatureBounds
import io.github.guyskv.signaturepad.core.model.SignatureStroke
import io.github.guyskv.signaturepad.core.smoothing.SignatureSmoothing
import io.github.guyskv.signaturepad.core.util.BoundsCalculator
import io.github.guyskv.signaturepad.core.util.PathLengthCalculator
import io.github.guyskv.signaturepad.core.validation.SignatureValidationConfig
import io.github.guyskv.signaturepad.core.validation.SignatureValidator

/**
 * Holds the state for a [SignaturePad] composable.
 *
 * Manages the list of strokes, undo/redo stacks, and provides
 * export and validation functionality.
 *
 * Create instances using [rememberSignaturePadState].
 *
 * @param validationConfig Configuration for signature validation thresholds.
 */
class SignaturePadState internal constructor(
    private val validationConfig: SignatureValidationConfig
) {
    private val _strokes = mutableStateListOf<SignatureStroke>()
    private val _redoStack = mutableStateListOf<SignatureStroke>()

    /** The current list of strokes in the signature. */
    val strokes: List<SignatureStroke> get() = _strokes.toList()

    /** True when there are no strokes. */
    val isEmpty: Boolean get() = _strokes.isEmpty()

    /**
     * True when the current signature passes all validation checks.
     *
     * A tiny dot, accidental tap, very small line, or extremely small
     * bounding box will be invalid. Use this property for Compose UI bindings.
     */
    val hasValidSignature: Boolean
        get() = SignatureValidator.validate(_strokes.toList(), validationConfig)

    /** True when there is at least one stroke that can be undone. */
    val canUndo: Boolean get() = _strokes.isNotEmpty()

    /** True when there is at least one stroke that can be redone. */
    val canRedo: Boolean get() = _redoStack.isNotEmpty()

    /** Internal smoothing level, set by the SignaturePad composable. */
    internal var smoothing: SignatureSmoothing = SignatureSmoothing.Medium

    /** Internal callback for notifying signature changes. */
    internal var onSignatureChanged: (() -> Unit)? = null

    /**
     * Clears all strokes and the redo stack.
     */
    fun clear() {
        _strokes.clear()
        _redoStack.clear()
        onSignatureChanged?.invoke()
    }

    /**
     * Undoes the last stroke, moving it to the redo stack.
     */
    fun undo() {
        if (_strokes.isNotEmpty()) {
            val last = _strokes.removeAt(_strokes.lastIndex)
            _redoStack.add(last)
            onSignatureChanged?.invoke()
        }
    }

    /**
     * Redoes the last undone stroke, moving it back to the strokes list.
     */
    fun redo() {
        if (_redoStack.isNotEmpty()) {
            val last = _redoStack.removeAt(_redoStack.lastIndex)
            _strokes.add(last)
            onSignatureChanged?.invoke()
        }
    }

    /**
     * Adds a new stroke. Clears the redo stack since a new action invalidates redo history.
     *
     * @param stroke The stroke to add.
     */
    fun addStroke(stroke: SignatureStroke) {
        _strokes.add(stroke)
        _redoStack.clear()
        onSignatureChanged?.invoke()
    }

    /**
     * Returns the bounding box of all strokes, or null if empty.
     */
    fun getBounds(): SignatureBounds? {
        return BoundsCalculator.calculateBounds(_strokes.toList())
    }

    /**
     * Returns the total number of points across all strokes.
     */
    fun getTotalPointCount(): Int {
        return _strokes.sumOf { it.points.size }
    }

    /**
     * Returns the total path length across all strokes.
     */
    fun getTotalPathLength(): Float {
        return PathLengthCalculator.calculateTotalPathLength(_strokes.toList())
    }

    /**
     * Validates whether the current signature meets all validation criteria.
     * Function form for non-Compose usage.
     */
    fun hasValidSignature(): Boolean {
        return SignatureValidator.validate(_strokes.toList(), validationConfig)
    }

    /**
     * Exports the signature as an SVG string.
     *
     * @param options SVG export options.
     * @return A valid SVG string.
     * @throws SignatureExportException If the signature is empty.
     */
    fun exportSvg(options: SignatureSvgExportOptions = SignatureSvgExportOptions()): String {
        return SignatureSvgExporter.export(_strokes.toList(), smoothing, options)
    }

    /**
     * Exports the signature as a JSON string.
     *
     * @return A JSON string representation of the signature.
     * @throws SignatureExportException If the signature is empty.
     */
    fun exportJson(): String {
        return SignatureJsonExporter.exportJson(_strokes.toList())
    }

    /**
     * Imports signature strokes from a JSON string.
     *
     * Clears existing strokes and redo stack, then loads the imported strokes.
     *
     * @param json The JSON string to import.
     */
    fun importJson(json: String) {
        val importedStrokes = SignatureJsonExporter.importJson(json)
        _strokes.clear()
        _redoStack.clear()
        _strokes.addAll(importedStrokes)
        onSignatureChanged?.invoke()
    }

    /**
     * Exports the signature as a PNG byte array.
     *
     * This is a suspend function because PNG rendering may be CPU-intensive.
     * The actual rendering is delegated to [PngExporter] in the compose module,
     * which uses Android's Bitmap and Canvas APIs.
     *
     * @param options PNG export options.
     * @return PNG image data as a ByteArray.
     * @throws SignatureExportException If the signature is empty.
     */
    suspend fun exportPng(options: SignaturePngExportOptions = SignaturePngExportOptions()): ByteArray {
        if (_strokes.isEmpty()) {
            throw SignatureExportException("Cannot export PNG: signature is empty.")
        }
        return PngExporter.export(_strokes.toList(), smoothing, options)
    }
}
