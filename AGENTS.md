# PropEditorX Status Overview

A specialized Java `.properties` file editor that supports automatic Unicode escape conversion (`\uXXXX` ↔ native characters), available as both an **Eclipse IDE plugin** and a **standalone SWT desktop application**.

- **Bundle Symbolic Name**: `io.github.xenogew.propedit`
- **Bundle Version**: 7.0.0.qualifier
- **Maintainer**: Santa Soft (inspired by Sou Miyazaki)
- **Status**: Modernized (Java 25, SWT, Tycho, CI/CD)

---

## Current Roadmap

### Phase 15: Functional Enhancement — Java to Properties Hyperlinking ✅ DONE
**Goal**: Enable `Ctrl + Click` (Hyperlinking) within Java string literals to jump directly to the corresponding property key in the `.properties` editor.

- **Spec Location**: `specs/20260315/hyperlinking_plan.md`

### Phase 16: Functional Enhancement — Properties to Java Find Usages ✅ DONE
**Goal**: Enable `Ctrl + Shift + G` within the `PropEditorX` editor to find all references of a property key in Java source code using Virtual Threads.

- **Spec Location**: `specs/20260315/find_usages_plan.md`

---

## Archived Phases
- **Phase 1-14**: Modernization completed.
  - See `specs/20260314/modernization_phases.md`

## Improvement Backlog
- See `specs/BACKLOG.md`
