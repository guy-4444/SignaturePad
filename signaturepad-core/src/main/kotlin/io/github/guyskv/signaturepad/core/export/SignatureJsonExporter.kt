package io.github.guyskv.signaturepad.core.export

import io.github.guyskv.signaturepad.core.model.SignatureColor
import io.github.guyskv.signaturepad.core.model.SignaturePoint
import io.github.guyskv.signaturepad.core.model.SignatureStroke
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * JSON data model for serializing/deserializing signature data.
 *
 * The format is versioned to support future schema changes.
 */
@Serializable
internal data class SignatureJsonData(
    val version: Int = 1,
    val strokes: List<JsonStroke>
)

@Serializable
internal data class JsonStroke(
    val color: String,
    val widthPx: Float,
    val points: List<JsonPoint>
)

@Serializable
internal data class JsonPoint(
    val x: Float,
    val y: Float,
    val timestampMillis: Long,
    val pressure: Float? = null
)

/**
 * Exports and imports signature data as JSON strings.
 */
object SignatureJsonExporter {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    /**
     * Exports the given strokes as a JSON string.
     *
     * @param strokes The strokes to export.
     * @return A JSON string representation of the signature.
     * @throws SignatureExportException If there are no strokes to export.
     */
    fun exportJson(strokes: List<SignatureStroke>): String {
        if (strokes.isEmpty()) {
            throw SignatureExportException("Cannot export JSON: signature is empty.")
        }

        val data = SignatureJsonData(
            version = 1,
            strokes = strokes.map { stroke ->
                JsonStroke(
                    color = stroke.color.toHexString(),
                    widthPx = stroke.widthPx,
                    points = stroke.points.map { point ->
                        JsonPoint(
                            x = point.x,
                            y = point.y,
                            timestampMillis = point.timestampMillis,
                            pressure = point.pressure
                        )
                    }
                )
            }
        )

        return json.encodeToString(data)
    }

    /**
     * Imports signature strokes from a JSON string.
     *
     * @param jsonString The JSON string to parse.
     * @return The list of [SignatureStroke]s parsed from the JSON.
     * @throws Exception If the JSON is malformed or incompatible.
     */
    fun importJson(jsonString: String): List<SignatureStroke> {
        val data = json.decodeFromString<SignatureJsonData>(jsonString)

        return data.strokes.map { jsonStroke ->
            SignatureStroke(
                color = SignatureColor.fromHexString(jsonStroke.color),
                widthPx = jsonStroke.widthPx,
                points = jsonStroke.points.map { jsonPoint ->
                    SignaturePoint(
                        x = jsonPoint.x,
                        y = jsonPoint.y,
                        timestampMillis = jsonPoint.timestampMillis,
                        pressure = jsonPoint.pressure
                    )
                }
            )
        }
    }
}
