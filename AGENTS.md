# Project Overview: PropEditorX

A specialized Java `.properties` file editor that focuses on **human-readable UTF-8 text**, available as both an **Eclipse IDE plugin** and a **standalone SWT desktop application**.

- **Bundle Symbolic Name**: `io.github.xenogew.propedit`
- **Bundle Version**: 7.0.0.qualifier
- **Maintainer**: Santa Soft (inspired by Sou Miyazaki)
- **Status**: Modernized (Java 25, SWT, Tycho, CI/CD)

---

## Current Roadmap

### Phase 15: Functional Enhancement — Java to Properties Hyperlinking ✅ DONE

**Goal**: Enable `Ctrl + Click` navigation from Java string literals to the corresponding property key.

### Phase 16: Functional Enhancement — Properties to Java Find Usages ✅ DONE

**Goal**: Enable `Ctrl + Shift + G` to find property key references in Java source code.

### Phase 17: Functional Enhancement — Dual-Mode Editing (Lokalise Style)

**Goal**: Implement a multi-page editor with a modern Grid View for side-by-side locale editing, maintaining strictly human-readable UTF-8 text.

- **Spec Location**: `specs/20260315/dual_mode_editor_plan.md`
- **Status**: Planning

### Phase 18: Technical Cleanup — Remove Legacy Unicode Escape Logic ✅ DONE

**Goal**: Purge the automatic `\uXXXX` ↔ native character conversion to ensure all files remain human-readable UTF-8 by default.

---

## Archived Phases

- **Phase 1-14**: Modernization completed.
  - See `specs/20260314/modernization_phases.md`

## Improvement Backlog

- See `specs/BACKLOG.md`
