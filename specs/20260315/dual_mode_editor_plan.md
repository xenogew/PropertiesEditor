# Phase 17: Dual-Mode Editing (Lokalise Style)

**Goal**: Implement a modern, side-by-side multi-locale editor that preserves native characters in **human-readable UTF-8**, eliminating legacy `\uXXXX` escape sequences.

## Technical Design

### 1. Architectural Shift: MultiPageEditorPart

`PropEditorX.java` will be refactored to extend `org.eclipse.ui.part.MultiPageEditorPart`.

- **Page 0 (Grid)**: A custom form-based UI for high-level translation management.
- **Page 1 (Source)**: The raw text editor for direct control.

### 2. Multi-Locale Data Model

The editor will discovery "sibling" properties files (e.g., `messages_ja.properties`) and load them using standard UTF-8 encoding.

- **NO Unicode Escaping**: All data will be loaded and saved as native characters.
- **Auto-Generation**: Editing a field marked as "EMPTY" will create the corresponding entry in the target locale file using native UTF-8 text.

### 3. Grid Page UI (Lokalise Style)

Using `org.eclipse.ui.forms.widgets.FormToolkit`:

- **Header**: Displays parent directory name as title and full absolute path as subtitle.
- **Key Cards**:
  - **Left**: Bold Key name.
  - **Right**: Vertical stack of editable textboxes per locale.
  - **"EMPTY" State**: Shown in **Purple Italics** when a key exists in the base file but not in a specific locale.
- **Styles**: Use Eclipse CSS styling, make UI elements like Editable Table (Excel-like) with bigger and proper Height & Width. No borders, no shadows, no gradients on the Textboxes.

### 4. Direct UTF-8 Saving

The saving process will be updated to:

1.  Bypass the `EncodeChanger` logic.
2.  Write native strings directly to the `.properties` files using the workspace's UTF-8 charset.

## Implementation Steps

1.  **Refactor Architecture**: Transition `PropEditorX` to a `MultiPageEditorPart`.
2.  **Disable Legacy Conversion**: Modify `PropertiesDocumentProvider` to stop performing `\uXXXX` conversion on load/save.
3.  **Build Grid UI**: Implement the `PropertiesGridPage` with the requested layout and "EMPTY" styling.
4.  **Implement Multi-File Save**: Handle saving across multiple locale files simultaneously.
5.  **Validation**: Verify that Japanese characters (like `財`) are saved exactly as `財` in the file on disk, not as `\u8ca1`.

---

# Phase 17.1: Grid UI Refinement & Aesthetic Styling

**Goal**: Optimize the Dual-Mode Grid layout to eliminate excessive whitespace and implement professional styling for locale headers, inspired by the Lokalise editor.

## 1. Background & Problem
The initial grid implementation had two main issues:
- **Verbose Unused Space**: The `rowHeight` calculation was too generous, leaving gaps.
- **UX Visibility**: The locale column lacked visual distinction as a header.

## 2. Proposed Solution

### A. Dynamic Height Optimization
Recalculate `rowHeight` to tightly fit content while maintaining comfortable padding.
- **Margins**: Set to **10px** (Tailwind `p-2` equivalent).
- **Row Formula**: `rowHeight = (numLocales * 40) + ((numLocales - 1) * 10) + 20`.

### B. Padding & Alignment
- **`p-2` equivalent**: Consistent **10px** margin to all cell containers.
- **`pl-2` equivalent**: **10px** horizontal spacing between Locale Label and Text Editor.
- **Top Alignment**: Ensure all inner components use `SWT.TOP`.

### C. Locale Header Styling
- **Bold Labels**: Apply Bold font variant.
- **Title Color**: Use `IFormColors.TITLE` color for locale names to distinguish them from values.

### D. Visual Debugging
- **Borders**: Implement 1px black borders using `PaintListener` to verify container bounds.

---

_Created on 2026-03-15_
