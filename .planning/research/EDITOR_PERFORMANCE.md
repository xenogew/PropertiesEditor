# Editor Performance: PropEditorX

**Domain:** Eclipse UI / SWT Performance
**Researched:** 2026-03-16
**Confidence:** HIGH

## Overview
For a properties editor handling large resource bundles, the choice between `SWT Table` and `ScrolledComposite` is critical. Research confirms that native widget overhead in `ScrolledComposite` leads to linear performance degradation, whereas `SWT.VIRTUAL` Tables provide constant-time responsiveness regardless of data size.

## Key Findings

### 1. Table vs. ScrolledComposite
| Feature | `ScrolledComposite` | `SWT Table` (Virtual) |
|---------|---------------------|-----------------------|
| **Scalability** | Poor (>50 rows lag) | Excellent (100k+ rows) |
| **Widget Count** | 1 per cell (High) | ~Visible cells only (Low) |
| **Memory** | High (Native handles) | Low (Managed by Table) |
| **Recommendation** | Avoid for Grid view. | **Mandatory** for Grid view. |

### 2. The "Flyweight" TableEditor Pattern
A common mistake is creating a `TableEditor` for every row. This negates the benefits of a virtual table.
*   **Best Practice:** Maintain **one** single `TableEditor` and **one** `Text` control instance for the entire table.
*   **Implementation:** When a user clicks/activates a cell, move the single editor to that cell's coordinate using `tableEditor.setEditor(text, item, column)`.
*   **Result:** Minimal memory footprint and instant activation.

### 3. MultiPageEditorPart Lazy Loading
To ensure the editor opens instantly even with heavy secondary tabs:
*   **Defer Creation:** Only initialize the primary (source) editor in `createPages()`.
*   **Lazy Tab Init:** Use the `pageChange(int newPageIndex)` event to trigger the creation of the Grid view controls only when the user first switches to that tab.
*   **Selection Provider:** Ensure `getSite().setSelectionProvider()` is updated during page changes to reflect the active page's context.

### 4. Responsiveness Patterns
*   **SWT.VIRTUAL:** Use `SWT.VIRTUAL` and `addListener(SWT.SetData, ...)` to populate rows only when they enter the viewport.
*   **Batch Redrawing:** Wrap heavy updates (like locale switching) in `table.setRedraw(false)` and `table.setRedraw(true)` to prevent layout thrashing.
*   **Background Loading:** Parse large property files in a background Job; use `Display.asyncExec()` only for the final UI binding.

## Sources
- Eclipse Foundation: "Virtual Tables and Trees"
- StackOverflow: "SWT TableEditor performance with many rows"
- Eclipse RCP Documentation: "Lazy loading in MultiPageEditorPart"
