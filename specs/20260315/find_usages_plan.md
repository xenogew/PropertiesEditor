# Phase 16: Properties to Java "Find Usages"

**Goal**: Enable `Ctrl + Shift + G` (standard Eclipse "References in Workspace" shortcut) within the `PropEditorX` editor to find all references of a property key in Java source code.

## Technical Design

### 1. Command and Handler

- **Command ID**: We will use the standard Eclipse references command ID: `org.eclipse.jdt.ui.edit.text.java.search.references.in.workspace`.
- **Handler**: Implement `io.github.xenogew.propedit.eclipse.plugin.action.FindUsagesAction`.
- **Active When**: The handler will be active only when the active editor is `io.github.xenogew.propedit.editors.PropEditorX`.

### 2. Key Identification

The handler will:

1.  Get the current text selection from the `PropEditorX` editor.
2.  If the selection is empty (just a cursor), expand it to the full property key on that line.
3.  Identify the property key (ignoring comments and values).

### 3. Search Integration

We will leverage the `org.eclipse.search.ui` API to perform a workspace-wide search:

- Use `NewSearchUI.runQueryInBackground(...)`.
- Implement a custom `ISearchQuery` (or use `TextSearchQueryProvider`) that searches for the property key within `*.java` files.
- The results will appear in the standard Eclipse **Search View**.

## Implementation Steps

1.  **Extract Key Logic**: Create a utility method in `PropertiesFileUtil` to identify the property key at a given offset in a `.properties` file.
2.  **Implement Handler**: Create `FindUsagesAction` implementing `org.eclipse.core.commands.IHandler`.
3.  **Search Logic**: Implement the bridge to the Eclipse Search API to trigger a text search for the key string in Java files.
4.  **Register Handler**: Update `plugin.xml` to bind the handler to the standard command ID and the `Ctrl + Shift + G` shortcut.
5.  **Thread Management**: Call the Search API directly from the UI thread. The Eclipse Search API (`NewSearchUI.runQueryInBackground`) automatically handles the actual search in a background job while ensuring the Search View UI is managed correctly on the UI thread.
6.  **Validation**: Test by pressing `Ctrl + Shift + G` on a key in `PropEditorX` and verifying that the Search View opens with Java file references.

---

_Created on 2026-03-15_
