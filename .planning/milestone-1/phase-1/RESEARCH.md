# Phase 1: Grid Performance Optimization - Research

**Researched:** 2026-03-16
**Domain:** Eclipse SWT Grid Virtualization & Performance
**Confidence:** HIGH

## Summary
For a high-performance, virtualized Properties Editor Grid requiring cell-spanning, **Nebula NatTable** is significantly superior to JFace TableViewer. While TableViewer is simpler for standard lists, it lacks native support for row merging and requires "Owner Draw" hacks that degrade performance at scale. NatTable provides a robust, layer-based architecture that handles 10k+ rows with ease and integrates with the e4 CSS engine for professional styling.

**Primary recommendation:** Use **Nebula NatTable** with `SpanningDataLayer` for the Key column and the `nattable.extension.e4` for styling.

## Standard Stack

### Core
| Library | Version | Purpose | Why Standard |
|---------|---------|---------|--------------|
| Nebula NatTable Core | 2.x | High-perf Grid | Virtualized by design; paints directly to Canvas. |
| NatTable e4 Extension| 2.x | CSS Styling | Native support for e4 CSS selectors and themes. |

### Supporting
| Library | Version | Purpose | When to Use |
|---------|---------|---------|--------------|
| Apache Commons Configuration | 2.x | I/O | preserving comments and file structure (Target for Phase 2). |

## Architecture Patterns

### Recommended Project Structure
1. **Data Layer**: Use `ISpanningDataProvider` to manage the multi-locale key/value map.
2. **Spanning Layer**: Wrap the data layer in a `SpanningDataLayer` to group locales under a single Property Key.
3. **Selection/Viewport Layers**: Standard NatTable layers for navigation and virtualization.
4. **Edit Layer**: Configures `ICellEditor` activation (Flyweight pattern).

### Pattern: Flyweight Editing
NatTable does not create widgets for every cell. When a user clicks a cell, the `EditLayer` looks up the configured `ICellEditor` for that cell type and activates it in-place. This ensures that even with 10k rows, only one editor widget exists at a time.

## Don't Hand-Roll

| Problem | Don't Build | Use Instead | Why |
|---------|-------------|-------------|-----|
| Cell Spanning | Manual drawing/hacks | `SpanningDataLayer` | Native, optimized support for merged cells. |
| Virtualization | `SWT.VIRTUAL` Table | NatTable | Better control over drawing priority and memory. |

## Common Pitfalls
- **TableViewer Rowspan:** Attempting rowspan in `TableViewer` via `TableEditor` will crash the UI thread with 10k rows. 
- **CSS Leakage:** Ensure `NatTableCSSHandler` is properly registered to avoid styles not applying to dynamically rendered cells.

## Validation Architecture
- **Performance Test:** Verify frame rate during fast scrolling with 10,000 keys (approx. 30,000-50,000 cells).
- **CSS Verification:** Use `Slate-400` (#94a3b8) for headers and `#e2e8f0` for grid lines in the CSS theme to match requirements.
