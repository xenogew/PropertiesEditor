# Research Summary: PropEditorX

**Domain:** Eclipse Plugin / i18n Properties Management
**Researched:** 2026-03-16
**Overall confidence:** HIGH

## Executive Summary
PropEditorX aims to revolutionize properties file management in Eclipse by bringing a "Lokalise-style" experience to Java developers. The research confirms that while the foundation (UTF-8, ResourceBundle) is mature, the existing tooling in the Eclipse ecosystem lacks the high-level coordination and user experience required for modern, large-scale i18n projects.

The project will focus on three main pillars: **Performance-driven Grid UI**, **Robust UTF-8/Structure-preserving I/O**, and **Intelligent i18n Validation**.

## Key Findings

### 1. UI & Performance (SWT/Eclipse)
- **Virtualization is Key**: For large bundles, `SWT.VIRTUAL` Tables are mandatory. `ScrolledComposite` does not scale beyond 50-100 rows without significant lag.
- **Flyweight Editor Pattern**: Using a single `TableEditor` and `Text` instance that moves between cells is the most performant way to handle thousands of editable fields.
- **Lazy Loading**: Multi-page editors should defer the creation of secondary tabs (like the Grid) until they are first activated to ensure instant file opening.

### 2. Properties Management & I/O
- **UTF-8 Transition**: Java 9+ supports UTF-8 by default for `ResourceBundle`, but `java.util.Properties.load(InputStream)` still fallbacks to ISO-8859-1. `Reader`/`Writer` must be used explicitly.
- **Structure Preservation**: Standard Java `Properties` class is destructive to comments and formatting. `Apache Commons Configuration 2` or a custom layout-aware parser is required to keep developer intent intact during programmatic writes.
- **Deterministic Output**: Git-friendly sorting (alphabetical or logical) is a highly requested feature to avoid unnecessary merge conflicts.

### 3. Java I18n Patterns
- **Standardization**: `ResourceBundle` remains the core, but `Spring MessageSource` is dominant in modern web development due to its flexibility.
- **Pain Points**: Missing keys across locales, placeholder mismatches (e.g., `{0}` missing in `ja` but present in `en`), and duplicate detection are the highest-value problems to solve.

## Competitive Edge (Lokalise-style)
- **Side-by-Side Editing**: The "Hero" feature. Vertical stacks of locales per key.
- **Visual Distinction**: Clear separation of keys and values, with header-like styling for locale names.
- **Unified Instance**: A single editor tab managing the entire bundle (implemented in Phase 17.3).

## Recommended Roadmap

### Milestone 1: Multi-Mode Foundation
- **Phase 1: Performance-Optimized Grid**: Transition to a virtual table with the flyweight editor pattern.
- **Phase 2: Robust I/O**: Implement structure-preserving save/load using layout-aware models.
- **Phase 3: Search & Filter**: Integrated search across all locales in the Grid view.

### Milestone 2: Intelligent i18n
- **Phase 4: Sync Validator**: Detect missing keys and placeholder mismatches.
- **Phase 5: Refactoring Suite**: Global key renaming and "Move to Bundle" actions.

## Confidence Assessment
| Area | Confidence | Notes |
|------|------------|-------|
| Performance | HIGH | SWT Virtual Table is a proven pattern. |
| Properties I/O| HIGH | Libraries like Apache Commons Config solve the comment loss issue. |
| User Pain | HIGH | I18n sync issues are universal in Java development. |

## Gaps to Address
- **Automated Translation**: Research into Machine Translation APIs (DeepL, Google) for future integration.
- **Visual Diffs**: How to provide a visual diff of properties changes within the Grid UI.
