package com.guy.signaturepad.screens

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guy.signaturepad.ui.theme.SignaturePadTheme
import io.github.guyskv.signaturepad.compose.SignaturePad
import io.github.guyskv.signaturepad.compose.SignaturePadConfig
import io.github.guyskv.signaturepad.compose.rememberSignaturePadState

/**
 * Signature in a form context with the Done button and validation feedback.
 * Demonstrates how hasValidSignature drives the done button state.
 */
@Composable
fun FormExampleScreen() {
    val signatureState = rememberSignaturePadState()
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Form Signature",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "The Done button is enabled only when the signature is valid (not a tiny dot or tap).",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Signature",
            style = MaterialTheme.typography.titleSmall
        )

        SignaturePad(
            state = signatureState,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
            config = SignaturePadConfig(
                strokeColor = Color(0xFF1A237E),
                strokeWidth = 2.5.dp,
                backgroundColor = Color(0xFFFAFAFA),
                cornerRadius = 12.dp,
                showClearButton = true,
                showDoneButton = true,
                showMiddleGuideline = true,
                guidelineColor = Color(0xFFE0E0E0)
            ),
            onDone = {
                Toast.makeText(
                    context,
                    "Signature accepted for: $name",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        // Validation status
        val statusText = when {
            signatureState.isEmpty -> "⏳ Please sign above"
            signatureState.hasValidSignature -> "✅ Signature is valid"
            else -> "⚠️ Signature too small — please draw more"
        }
        val statusColor = when {
            signatureState.isEmpty -> MaterialTheme.colorScheme.onSurfaceVariant
            signatureState.hasValidSignature -> Color(0xFF2E7D32)
            else -> Color(0xFFE65100)
        }

        Text(
            text = statusText,
            style = MaterialTheme.typography.bodyMedium,
            color = statusColor
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FormExampleScreenPreview() {
    SignaturePadTheme {
        FormExampleScreen()
    }
}
