# Requirements: PropEditorX

## Milestone 1: Multi-Mode Foundation (Completed/Refining)
*Requirement tracking for the initial dual-mode implementation.*

- [x] **REQ-1.1**: Multi-Page Editor architecture (`Editor::{✘}` + Source tabs).
- [x] **REQ-1.2**: Native UTF-8 support (no `\uXXXX` escaping).
- [x] **REQ-1.3**: Side-by-side Grid editing with top-aligned keys.
- [x] **REQ-1.4**: Dynamic locale discovery and tab management.
- [x] **REQ-1.5**: Robust save synchronization between tabs.
- [ ] **REQ-1.6**: **Performance Optimization**: Transition Grid to `SWT.VIRTUAL` Table to support large bundles.
- [ ] **REQ-1.7**: **Flyweight Editor**: Implement a single moving `TableEditor` instance for Grid performance.

## Milestone 2: Intelligent i18n & Search
*Advanced features to differentiate PropEditorX from standard editors.*

- **REQ-2.1: Structure-Preserving I/O**
    - Preservation of human-authored comments and blank lines during Grid saves.
    - Integration of a layout-aware properties model (e.g., Apache Commons Configuration 2).
- **REQ-2.2: Sync Validator**
    - Visual indicators for missing keys in specific locales.
    - Detection of placeholder mismatches (e.g., `{0}` present in `en` but missing in `ko`).
- **REQ-2.3: Global Grid Search**
    - Instant filtering of keys and values across all locales.
    - Highlight matches in both the Grid and Source views.
- **REQ-2.4: Deterministic Sorting**
    - Configurable sorting (Alphabetical vs. Original File Order).
    - Ensures Git-friendly output.

## Milestone 3: Advanced UX & Automation
*Future-looking features for total i18n dominance.*

- **REQ-3.1: Machine Translation Integration**
    - Integration with DeepL/Google Translate for "one-click" initial translations.
- **REQ-3.2: Visual Diffs**
    - Side-by-side comparison of current vs. saved state within the Grid.
- **REQ-3.3: Global Refactoring**
    - "Rename Key" action that updates all files in the bundle and related Java source code.

## Non-Functional Requirements
- **NFR-1**: Responsiveness - Grid scrolling must remain smooth (>60fps) for bundles up to 5,000 keys.
- **NFR-2**: Stability - No `IllegalStateException` or `SWTException` during background file changes.
- **NFR-3**: Compatibility - Support Eclipse versions back to 2023-03 and Java 17+.
