package com.guy.signaturepad.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guy.signaturepad.ui.theme.SignaturePadTheme
import io.github.guyskv.signaturepad.compose.SignaturePad
import io.github.guyskv.signaturepad.compose.SignaturePadConfig
import io.github.guyskv.signaturepad.compose.rememberSignaturePadState

/**
 * Basic signature pad example with clear button and undo/redo controls.
 */
@Composable
fun BasicExampleScreen() {
    val signatureState = rememberSignaturePadState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Basic Signature",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Draw your signature below. The clear button appears when you start drawing.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        SignaturePad(
            state = signatureState,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp)),
            config = SignaturePadConfig(
                strokeColor = Color.Black,
                strokeWidth = 3.dp,
                backgroundColor = Color.White,
                cornerRadius = 16.dp,
                showClearButton = true,
                showDoneButton = false
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { signatureState.undo() },
                enabled = signatureState.canUndo,
                modifier = Modifier.weight(1f)
            ) {
                Text("Undo")
            }

            OutlinedButton(
                onClick = { signatureState.redo() },
                enabled = signatureState.canRedo,
                modifier = Modifier.weight(1f)
            ) {
                Text("Redo")
            }

            OutlinedButton(
                onClick = { signatureState.clear() },
                enabled = !signatureState.isEmpty,
                modifier = Modifier.weight(1f)
            ) {
                Text("Clear")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Status info
        Text(
            text = buildString {
                append("Empty: ${signatureState.isEmpty}")
                append(" • Valid: ${signatureState.hasValidSignature}")
                append(" • Points: ${signatureState.getTotalPointCount()}")
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BasicExampleScreenPreview() {
    SignaturePadTheme {
        BasicExampleScreen()
    }
}
