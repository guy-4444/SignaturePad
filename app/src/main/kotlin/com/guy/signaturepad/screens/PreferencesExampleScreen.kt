package com.guy.signaturepad.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guy.signaturepad.ui.theme.SignaturePadTheme
import io.github.guyskv.signaturepad.compose.SignaturePad
import io.github.guyskv.signaturepad.compose.SignaturePadConfig
import io.github.guyskv.signaturepad.compose.rememberSignaturePadState
import io.github.guyskv.signaturepad.core.smoothing.SignatureSmoothing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesExampleScreen() {
    val state = rememberSignaturePadState()
    
    // Preferences State
    var strokeColor by remember { mutableStateOf(Color.Black) }
    var backgroundColor by remember { mutableStateOf(Color.White) }
    var strokeWidth by remember { mutableFloatStateOf(3f) }
    var cornerRadius by remember { mutableFloatStateOf(16f) }
    var smoothing by remember { mutableStateOf(SignatureSmoothing.Medium) }
    var showMiddleGuideline by remember { mutableStateOf(false) }
    var showClearButton by remember { mutableStateOf(true) }
    var showDoneButton by remember { mutableStateOf(false) }
    
    // "Read-only mode" sets enabled to false
    var isReadOnly by remember { mutableStateOf(false) }
    // "Accessible" toggle (adds semantic description to the whole column for demo purposes)
    var isAccessible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .then(if (isAccessible) Modifier.semantics { contentDescription = "Preferences Configuration Screen" } else Modifier)
    ) {
        Text(
            text = "Preferences Playground",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tweak settings and see them update live below.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Live Preview SignaturePad
        SignaturePad(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(cornerRadius.dp)),
            config = SignaturePadConfig(
                strokeColor = strokeColor,
                backgroundColor = backgroundColor,
                strokeWidth = strokeWidth.dp,
                cornerRadius = cornerRadius.dp,
                smoothing = smoothing,
                showMiddleGuideline = showMiddleGuideline,
                showClearButton = showClearButton,
                showDoneButton = showDoneButton,
                enabled = !isReadOnly
            )
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        // Colors
        Text("Colors", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Stroke: ", modifier = Modifier.weight(1f))
            ColorPickerRow(
                selectedColor = strokeColor,
                onColorSelected = { strokeColor = it },
                colors = listOf(Color.Black, Color.Red, Color.Blue, Color(0xFF1B5E20))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Background: ", modifier = Modifier.weight(1f))
            ColorPickerRow(
                selectedColor = backgroundColor,
                onColorSelected = { backgroundColor = it },
                colors = listOf(Color.White, Color.LightGray, Color(0xFFFFF8E1), Color(0xFFE8F5E9))
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Sliders
        Text("Dimensions", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Stroke Width: ${strokeWidth.toInt()}dp")
        Slider(
            value = strokeWidth,
            onValueChange = { strokeWidth = it },
            valueRange = 1f..10f
        )
        Text("Corner Radius: ${cornerRadius.toInt()}dp")
        Slider(
            value = cornerRadius,
            onValueChange = { cornerRadius = it },
            valueRange = 0f..32f
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Smoothing Dropdown
        var expanded by remember { mutableStateOf(false) }
        Text("Smoothing Level", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = smoothing.name,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                SignatureSmoothing.entries.forEach { level ->
                    DropdownMenuItem(
                        text = { Text(level.name) },
                        onClick = {
                            smoothing = level
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Switches
        Text("Features & Controls", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        SwitchRow("Show Middle Guideline", showMiddleGuideline) { showMiddleGuideline = it }
        SwitchRow("Show Clear Button", showClearButton) { showClearButton = it }
        SwitchRow("Show Done Button", showDoneButton) { showDoneButton = it }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Modes", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        SwitchRow("Read-Only Mode", isReadOnly) { isReadOnly = it }
        SwitchRow("Accessible Mode", isAccessible) { isAccessible = it }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ColorPickerRow(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    colors: List<Color>
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(
                        width = if (selectedColor == color) 3.dp else 1.dp,
                        color = if (selectedColor == color) MaterialTheme.colorScheme.primary else Color.Gray,
                        shape = CircleShape
                    )
                    .clickable { onColorSelected(color) }
            )
        }
    }
}

@Composable
fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreferencesExampleScreenPreview() {
    SignaturePadTheme {
        PreferencesExampleScreen()
    }
}
