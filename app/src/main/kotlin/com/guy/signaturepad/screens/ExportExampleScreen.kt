package com.guy.signaturepad.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.Alignment
import com.guy.signaturepad.ui.theme.SignaturePadTheme
import io.github.guyskv.signaturepad.compose.SignaturePad
import io.github.guyskv.signaturepad.compose.SignaturePadConfig
import io.github.guyskv.signaturepad.compose.SignaturePadPreview
import io.github.guyskv.signaturepad.compose.SignaturePadPreviewConfig
import io.github.guyskv.signaturepad.compose.rememberSignaturePadState
import io.github.guyskv.signaturepad.core.model.SignatureStroke
import kotlinx.coroutines.launch

/**
 * Export example demonstrating PNG, SVG, and JSON export/import.
 */
@Composable
fun ExportExampleScreen() {
    val signatureState = rememberSignaturePadState()
    val scope = rememberCoroutineScope()
    var exportResult by remember { mutableStateOf("") }
    var exportType by remember { mutableStateOf("") }
    var previewStrokes by remember { mutableStateOf<List<SignatureStroke>>(emptyList()) }
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Export Signature",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Draw a signature, then export as PNG, SVG, or JSON.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        SignaturePad(
            state = signatureState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
            config = SignaturePadConfig(
                backgroundColor = Color.White,
                cornerRadius = 12.dp,
                showClearButton = true
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        try {
                            val bytes = signatureState.exportPng()
                            exportResult = "PNG exported: ${bytes.size} bytes"
                            exportType = "PNG"
                        } catch (e: Exception) {
                            exportResult = "Error: ${e.message}"
                            exportType = "Error"
                        }
                    }
                },
                enabled = !signatureState.isEmpty,
                modifier = Modifier.weight(1f)
            ) {
                Text("PNG")
            }

            Button(
                onClick = {
                    try {
                        val svg = signatureState.exportSvg()
                        exportResult = svg
                        exportType = "SVG"
                    } catch (e: Exception) {
                        exportResult = "Error: ${e.message}"
                        exportType = "Error"
                    }
                },
                enabled = !signatureState.isEmpty,
                modifier = Modifier.weight(1f)
            ) {
                Text("SVG")
            }

            Button(
                onClick = {
                    try {
                        val json = signatureState.exportJson()
                        exportResult = json
                        exportType = "JSON"
                    } catch (e: Exception) {
                        exportResult = "Error: ${e.message}"
                        exportType = "Error"
                    }
                },
                enabled = !signatureState.isEmpty,
                modifier = Modifier.weight(1f)
            ) {
                Text("JSON")
            }
        }

        // Import JSON to preview
        if (exportType == "JSON" && exportResult.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    try {
                        signatureState.importJson(exportResult)
                        previewStrokes = signatureState.strokes
                    } catch (e: Exception) {
                        exportResult = "Import error: ${e.message}"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Import JSON → Preview")
            }
        }

        // Show preview from imported JSON
        if (previewStrokes.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Read-Only Preview",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            SignaturePadPreview(
                strokes = previewStrokes,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
                config = SignaturePadPreviewConfig(
                    backgroundColor = Color(0xFFF0F0F0),
                    cornerRadius = 12.dp
                )
            )
        }

        // Export result display
        if (exportResult.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Export Result ($exportType):",
                    style = MaterialTheme.typography.titleSmall
                )
                
                OutlinedButton(onClick = {
                    clipboardManager.setText(AnnotatedString(exportResult))
                }) {
                    Text("Copy")
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (exportResult.length > 2000) exportResult.take(2000) + "..." else exportResult,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ExportExampleScreenPreview() {
    SignaturePadTheme {
        ExportExampleScreen()
    }
}
