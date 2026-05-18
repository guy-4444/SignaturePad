package com.guy.signaturepad.screens

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

/**
 * Demonstrates the middle horizontal guideline feature.
 */
@Composable
fun GuidelineExampleScreen() {
    val signatureState = rememberSignaturePadState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Guideline",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "The horizontal guideline helps users align their signature. It appears behind the strokes and respects corner clipping.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // With guideline
        Text("With guideline", style = MaterialTheme.typography.titleSmall)
        SignaturePad(
            state = signatureState,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp)),
            config = SignaturePadConfig(
                strokeColor = Color.Black,
                strokeWidth = 3.dp,
                backgroundColor = Color.White,
                cornerRadius = 16.dp,
                showClearButton = true,
                showMiddleGuideline = true,
                guidelineColor = Color(0xFFBDBDBD),
                guidelineStrokeWidth = 1.dp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Without guideline for comparison
        Text("Without guideline (comparison)", style = MaterialTheme.typography.titleSmall)
        SignaturePad(
            state = rememberSignaturePadState(),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp)),
            config = SignaturePadConfig(
                strokeColor = Color.Black,
                strokeWidth = 3.dp,
                backgroundColor = Color.White,
                cornerRadius = 16.dp,
                showClearButton = true,
                showMiddleGuideline = false
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GuidelineExampleScreenPreview() {
    SignaturePadTheme {
        GuidelineExampleScreen()
    }
}
