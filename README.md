# ComposeSignaturePad

A production-ready Jetpack Compose library for capturing, validating, and exporting handwritten signatures on Android.

## Features

- 🖊️ **Smooth signature drawing** — Quadratic Bézier smoothing for natural-looking strokes
- ✅ **Smart validation** — Rejects accidental taps and tiny dots, accepts real signatures
- ↩️ **Undo / Redo** — Full stroke-level undo and redo support
- 📤 **Multi-format export** — PNG, SVG, and JSON export
- 📥 **JSON import** — Load and display previously saved signatures
- 👁️ **Read-only preview** — Display saved signatures without editing
- 🎨 **Highly customizable** — Colors, stroke width, corner radius, smoothing level
- 📏 **Optional guideline** — Horizontal middle guideline for alignment
- 🔘 **Built-in controls** — Optional clear and done buttons (no Material3 required)
- ♿ **Accessible** — Semantic labels for screen readers

## Installation

Add JitPack to your project's repository configuration (usually in `settings.gradle.kts`):

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

Then, add the dependency to your module-level `build.gradle.kts`:

```kotlin
dependencies {
    // For the full Compose UI experience
    implementation("com.github.guy-4444.SignaturePad:signaturepad-compose:1.00.01")
    
    // Or if you only need the headless core models
    // implementation("com.github.guy-4444.SignaturePad:signaturepad-core:1.00.01")
}
```

## Basic Usage

```kotlin
val signatureState = rememberSignaturePadState()

SignaturePad(
    state = signatureState,
    modifier = Modifier
        .fillMaxWidth()
        .height(260.dp),
    config = SignaturePadConfig(
        strokeColor = Color.Black,
        strokeWidth = 3.dp,
        backgroundColor = Color.White,
        cornerRadius = 16.dp,
        showClearButton = true
    )
)
```

## Usage in XML Layouts

Even though this library is built with Compose, you can easily use it in traditional XML layouts using a `ComposeView`.

**1. Add a ComposeView to your XML layout (`activity_main.xml`):**
```xml
<androidx.compose.ui.platform.ComposeView
    android:id="@+id/signaturePadView"
    android:layout_width="match_parent"
    android:layout_height="260dp" />
```

**2. Set the content in your Activity or Fragment:**
```kotlin
val composeView = findViewById<ComposeView>(R.id.signaturePadView)

composeView.setContent {
    val signatureState = rememberSignaturePadState()
    
    SignaturePad(
        state = signatureState,
        modifier = Modifier.fillMaxSize(),
        config = SignaturePadConfig(
            strokeColor = Color.Black,
            strokeWidth = 3.dp,
            backgroundColor = Color.White,
            cornerRadius = 16.dp
        )
    )
}
```

## Clear Button

The clear button appears automatically when the signature is not empty:

```kotlin
SignaturePad(
    state = signatureState,
    config = SignaturePadConfig(
        showClearButton = true,
        clearButtonAlignment = Alignment.TopEnd,
        clearButtonSize = 32.dp
    )
)
```

## Done Button

The done button is hidden by default. When enabled, it is only active when the signature passes validation:

```kotlin
SignaturePad(
    state = signatureState,
    config = SignaturePadConfig(
        showDoneButton = true,
        doneButtonAlignment = Alignment.BottomEnd
    ),
    onDone = {
        // Signature is valid and user tapped done
    }
)
```

## Validation

Validation rejects accidental taps, tiny dots, and minimal strokes:

```kotlin
val signatureState = rememberSignaturePadState(
    validationConfig = SignatureValidationConfig(
        minStrokeCount = 1,
        minTotalPointCount = 8,
        minBoundingBoxWidthPx = 48f,
        minBoundingBoxHeightPx = 16f,
        minTotalPathLengthPx = 120f
    )
)

// In your UI:
if (signatureState.hasValidSignature) {
    Text("✅ Valid signature")
} else {
    Text("⚠️ Please draw a real signature")
}
```

## Export PNG

```kotlin
val scope = rememberCoroutineScope()

scope.launch {
    val pngBytes = signatureState.exportPng(
        SignaturePngExportOptions(
            transparentBackground = true,
            trimToSignature = true,
            paddingPx = 24,
            scale = 2f
        )
    )
    // Save pngBytes to file or upload
}
```

## Export SVG

```kotlin
val svgString = signatureState.exportSvg(
    SignatureSvgExportOptions(
        trimToSignature = true,
        paddingPx = 24,
        includeBackground = false
    )
)
```

## Export / Import JSON

```kotlin
// Export
val json = signatureState.exportJson()

// Import
signatureState.importJson(json)
```

JSON format:

```json
{
  "version": 1,
  "strokes": [
    {
      "color": "#FF000000",
      "widthPx": 8.0,
      "points": [
        { "x": 10.0, "y": 20.0, "timestampMillis": 123456789, "pressure": null }
      ]
    }
  ]
}
```

## Customization

```kotlin
SignaturePad(
    state = signatureState,
    config = SignaturePadConfig(
        strokeColor = Color(0xFF1565C0),   // Blue ink
        strokeWidth = 4.dp,
        backgroundColor = Color(0xFFFFF8E1), // Cream background
        cornerRadius = 24.dp,
        showMiddleGuideline = true,
        guidelineColor = Color(0xFFE0E0E0),
        smoothing = SignatureSmoothing.High,
        clipToBounds = true
    )
)
```

## Read-Only Preview

```kotlin
SignaturePadPreview(
    strokes = savedStrokes,
    modifier = Modifier
        .fillMaxWidth()
        .height(150.dp),
    config = SignaturePadPreviewConfig(
        backgroundColor = Color(0xFFF0F0F0),
        cornerRadius = 12.dp
    )
)
```

## Undo / Redo

```kotlin
Button(onClick = { signatureState.undo() }, enabled = signatureState.canUndo) {
    Text("Undo")
}

Button(onClick = { signatureState.redo() }, enabled = signatureState.canRedo) {
    Text("Redo")
}
```

## Without Built-in Buttons

```kotlin
SignaturePad(
    state = signatureState,
    config = SignaturePadConfig(
        showClearButton = false,
        showDoneButton = false
    )
)
```

## Architecture

```
signaturepad-core/          Pure Kotlin — models, validation, SVG/JSON export, smoothing
signaturepad-compose/       Compose UI — state, pad, preview, PNG export, controls
app/                        Sample app demonstrating all features
```

## Limitations

This library captures drawn signatures only. It does not provide:

- Legal verification
- Identity verification
- Biometric validation
- Encryption
- PDF signing
- Cloud storage
- Handwriting recognition

## License

```
Copyright 2024 guyskv

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
