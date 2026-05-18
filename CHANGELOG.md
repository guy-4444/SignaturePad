# Changelog

All notable changes to ComposeSignaturePad will be documented in this file.

## [1.0.0] - 2024-01-01

### Added
- Initial release of ComposeSignaturePad library
- `SignaturePad` composable with touch/stylus input capture
- `SignaturePadState` with full undo/redo support
- Smart signature validation with configurable thresholds
- Path smoothing with quadratic Bézier curves (None/Low/Medium/High)
- PNG export with transparency, trimming, padding, and scale options
- SVG export with path data and stroke preservation
- JSON export/import with versioned format
- `SignaturePadPreview` read-only composable
- Built-in clear button (auto-shows when not empty)
- Built-in done button (enabled only with valid signature)
- Middle horizontal guideline option
- Corner radius clipping
- Full customization: colors, stroke width, background, corner radius
- Accessibility labels for screen readers
- Sample app with 5 example screens
