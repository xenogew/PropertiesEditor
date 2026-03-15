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

# Phase 17.2: Locale Header Aesthetic Refinement

**Goal**: Strengthen the visual identity of the locale header column by applying a bold font and a distinct background color (Tailwind `slate-400` equivalent).

## 1. Background & UX Problem
Initial implementation lacked visual distinction for the locale column, making it blend into editable areas.

## 2. Proposed Solution

### A. Color Management
- **Target Color**: Tailwind `slate-400` (RGB: 148, 163, 184).
- **Implementation**: Manage via `ColorManager` within `PropEditorX`.

### B. Header Styling
- **Bold Labels**: Explicitly set Bold font.
- **Background**: Apply the `slate-400` color to the Locale Label.
- **Fill Layout**: Ensure labels fill their vertical space for a "column" effect.

### C. Resource Lifecycle
- Ensure `ColorManager` is properly disposed of in `PropEditorX.dispose()`.

---

# Phase 17.3: Unified Editor Instance (Bundle-Aware)

**Goal**: Prevent multiple editor tabs from opening for different files that belong to the same property bundle. Opening any sibling file should activate the existing editor for that bundle instead of creating a new tab.

## 1. Problem Statement
Eclipse treats each `.properties` file as a distinct resource, opening separate tabs for `messages.properties` and `messages_ja.properties` despite them being part of the same bundle.

## 2. Proposed Solution

### A. Implement `IEditorMatchingStrategy`
Created `PropertyEditorMatchingStrategy` to compare editor inputs based on:
1.  **Directory**: Files must be in the same parent folder.
2.  **Base Name**: Files must share the same base bundle name (e.g., "messages").

### B. Utility Centralization
Moved `extractBaseName` logic to `PropertiesFileUtil` to provide a single point of truth for bundle identification.

### C. Registration
Registered the matching strategy in `plugin.xml` under the `PropEditorX` editor definition.

---

# Phase 17.4: Dynamic Locale Discovery & Tab Management

**Goal**: Ensure that newly created locale files (e.g., `messages_ko.properties`) are correctly identified with human-readable names and automatically appear as "Source" tabs in the editor when opened. Also, establish a unique branding for the multi-mode tab.

## 1. Branding & UX
- **Tab Name**: Renamed the main Grid tab from "Grid" to **"Editor::{✘}"** using the Java method reference pattern (`::`) and scope brackets (`{}`) for a unique, developer-centric brand identity.

## 2. Robust Locale Name Resolution
- **`java.util.Locale` Integration**: Updated `PropertyBundleModel.getDisplayName` to use standard Java locale resolution.
- **Result**: `ko` -> "Korean", `ja` -> "Japanese", etc., ensuring all international locales are human-readable.

## 3. Dynamic Tab Management
- **On-the-fly Updates**: Modified `PropEditorX.init` to detect when a sibling file from the same bundle is opened and dynamically add its "Source" tab.
- **Live Grid Refresh**: Implemented `refresh()` in `PropertiesGridPage` to rebuild the UI when new locales are detected, ensuring the model and view are always in sync.

---

# Phase 17.5: Robust Save Synchronization & Logging

**Goal**: Solve the data-desync flaw during saves and implement a standardized logging framework.

## 1. Save Synchronization
Modified `PropEditorX.doSave()` to prioritize Source tab edits while keeping the Grid in sync.
- **Workflow**: 
    1.  **Commit Source**: Save all open text editors to disk first.
    2.  **Model Sync**: Reload the `PropertyBundleModel` from disk immediately after source save.
    3.  **Conditional Grid Save**: Only perform `bundleModel.saveAll()` if the Grid itself was modified (`isDirty`).
    4.  **UI Refresh**: Trigger a Grid UI rebuild to reflect the new Source content.

## 2. Professional Logging
Implemented a project-wide logging strategy using the Eclipse `ILog` engine.
- **`PropEditorXPlugin.error(msg, e)`**: Added a convenience method for standardized error reporting.
- **Audit**: Replaced all empty or comment-only `catch` blocks in critical paths (I/O, Page Init, Model Refresh) with proper log entries.

---

_Created on 2026-03-15_
