# Architecture Patterns: PropEditorX

**Domain:** Eclipse Plugin Architecture
**Researched:** 2024-05-22

## Recommended Architecture

A **Multi-Page Editor** (using `org.eclipse.ui.part.MultiPageEditorPart`) with a shared data model.

### Component Boundaries

| Component | Responsibility | Communicates With |
|-----------|---------------|-------------------|
| **ResourceBundleModel** | Maintains the parsed keys, values, and comments for all locales. | RawEditor, GridEditor, Validator |
| **RawEditor** | Standard Eclipse text editor for one `.properties` file. | ResourceBundleModel (via sync) |
| **GridEditor** | Custom SWT Table/Grid for side-by-side editing. | ResourceBundleModel (via data binding) |
| **Validator** | Runs checks for missing/duplicate keys or placeholder errors. | ResourceBundleModel, Marker View |

### Data Flow

```
File Change → RawEditor → Update ResourceBundleModel → Refresh GridEditor
Grid Edit → Update ResourceBundleModel → Sync back to RawEditor → Save to File
```

## Patterns to Follow

### Pattern 1: Command Buffer Syncing
**What:** Changes in the Grid view should be converted into commands that apply edits to the underlying IDocument of the Raw Editor.
**When:** Whenever a cell in the Grid is modified.
**Benefits:** Ensures Undo/Redo history is maintained via the standard Eclipse framework.

### Pattern 2: Delta Monitoring
**What:** Listen for workspace changes to detect if `.properties` files are modified externally.
**When:** Application-wide.
**Example:** `IResourceChangeListener` in Eclipse.

## Anti-Patterns to Avoid

### Anti-Pattern 1: `java.util.Properties.store()`
**What:** Using the standard library to save the file.
**Why bad:** It strips comments, reorders keys, and loses custom formatting.
**Instead:** Use a custom parser or `Apache Commons Configuration` that respects the file's original structure.

### Anti-Pattern 2: Global State
**What:** Storing the model in a static singleton.
**Why bad:** Prevents multiple editor instances from working independently.
**Instead:** Scoped model per editor instance.

## Scalability Considerations

| Concern | 100 Keys | 10K Keys | 1M Keys |
|---------|----------|----------|---------|
| Memory Usage | Negligible. | Noticeable but manageable. | Requires virtual tables/lazy loading. |
| Grid Rendering | Fast. | May lag if all rows are rendered at once. | MUST use `SWT.VIRTUAL` tables. |
| Search | Instant. | Minor delay (indexing recommended). | Requires background indexing. |

## Sources

- [Eclipse Plugin Development Guide - Editors](https://help.eclipse.org/latest/topic/org.eclipse.platform.doc.isv/guide/editors.htm)
- [SWT Virtual Table Pattern](https://www.eclipse.org/articles/Article-SWT-Virtual/Virtual-Tables-Trees.html)
- [Eclipse Resource Bundle Editor Source Code Patterns (Legacy Reference)]
