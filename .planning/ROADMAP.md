# Roadmap: PropEditorX

## Milestone 1: Multi-Mode Foundation
**Status**: Finalizing
**Goal**: Establish a rock-solid, high-performance base for the multi-locale editor.

### Phase 1: Grid Performance Optimization
- Transition `PropertiesGridPage` to use **Nebula NatTable**.
- Implement `SpanningDataLayer` for Key column rowspan effects.
- Implement **Flyweight Editor Pattern**: A single active editor jumping between cells.
- Native virtualization for 100k+ rows with minimal memory overhead.
- **Success Criteria**: Smooth scrolling with 1,000+ keys; immediate opening of large bundles.

## Milestone 2: Intelligent i18n & Production Readiness
**Goal**: Make the editor robust for professional production use.

### Phase 2: Structure-Preserving I/O
- Integrate `Apache Commons Configuration 2` or custom layout-aware parser.
- Ensure `// Comments` and blank lines are preserved during Grid saves.
- Implement **Deterministic Sorting** (Alpha vs Logical) to keep Git diffs clean.
- **Success Criteria**: No comment loss after Grid editing; predictable file output.

### Phase 3: Global Search & Navigation
- Add search/filter bar to the `Editor::{✘}` tab.
- Implement real-time filtering across all locales.
- Add "Go to Source" action to jump from Grid cell to the exact line in Source tab.
- **Success Criteria**: User can find any value across 10 locales in <1 second.

### Phase 4: Sync Validator (The i18n "Brain")
- Implement background validation for missing keys across the bundle.
- Implement **Placeholder Guard**: Alert user if `{0}` is missing in a translation.
- Add visual decorators (error/warning icons) to Grid rows.
- **Success Criteria**: Zero runtime errors from missing i18n keys or mismatched placeholders.

## Milestone 3: Advanced Automation
**Goal**: Leverage AI and platform depth for maximum productivity.

### Phase 5: Machine Translation & Automation
- Integration with external MT providers (DeepL/Google).
- "Auto-fill Missing" action for quick initial drafts.
- Implementation of **Visual Diffs** for local history.

---
*Future ideas: YAML/JSON support, JDT-integrated refactoring.*
