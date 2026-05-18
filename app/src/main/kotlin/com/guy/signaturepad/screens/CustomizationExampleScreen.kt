package com.guy.signaturepad.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.guy.signaturepad.ui.theme.SignaturePadTheme
import io.github.guyskv.signaturepad.compose.SignaturePad
import io.github.guyskv.signaturepad.compose.SignaturePadConfig
import io.github.guyskv.signaturepad.compose.rememberSignaturePadState
import io.github.guyskv.signaturepad.core.smoothing.SignatureSmoothing

/**
 * Demonstrates different visual customization options.
 */
@Composable
fun CustomizationExampleScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Customization",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Different colors, corner radii, stroke widths, and smoothing levels.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Blue ink on cream background, large corner radius
        Text("Blue ink, large radius", style = MaterialTheme.typography.titleSmall)
        SignaturePad(
            state = rememberSignaturePadState(),
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .border(1.dp, Color(0xFF90CAF9), RoundedCornerShape(24.dp)),
            config = SignaturePadConfig(
                strokeColor = Color(0xFF1565C0),
                strokeWidth = 4.dp,
                backgroundColor = Color(0xFFFFF8E1),
                cornerRadius = 24.dp,
                showClearButton = true,
                smoothing = SignatureSmoothing.High
            )
        )

        // Red ink on light gray, no smoothing
        Text("Red ink, no smoothing", style = MaterialTheme.typography.titleSmall)
        SignaturePad(
            state = rememberSignaturePadState(),
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .border(1.dp, Color(0xFFEF9A9A), RoundedCornerShape(8.dp)),
            config = SignaturePadConfig(
                strokeColor = Color(0xFFC62828),
                strokeWidth = 2.dp,
                backgroundColor = Color(0xFFF5F5F5),
                cornerRadius = 8.dp,
                showClearButton = true,
                smoothing = SignatureSmoothing.None
            )
        )

        // Thick dark green ink, medium corner radius
        Text("Green ink, thick stroke", style = MaterialTheme.typography.titleSmall)
        SignaturePad(
            state = rememberSignaturePadState(),
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .border(1.dp, Color(0xFFA5D6A7), RoundedCornerShape(16.dp)),
            config = SignaturePadConfig(
                strokeColor = Color(0xFF1B5E20),
                strokeWidth = 5.dp,
                backgroundColor = Color(0xFFE8F5E9),
                cornerRadius = 16.dp,
                showClearButton = true,
                smoothing = SignatureSmoothing.Medium
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CustomizationExampleScreenPreview() {
    SignaturePadTheme {
        CustomizationExampleScreen()
    }
}
