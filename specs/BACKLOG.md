# PropEditorX Improvements Backlog

These are the proposed ideas to enhance the Eclipse Plugin functionality.

1. Bidirectional "Deep Link" Navigation (JDT Integration)
   Currently, we have hover and completion in Java files, but we can make it truly "IDE-native":

- Hyperlinking (Ctrl + Click): Allow users to Ctrl + Click a property key in a Java string literal to jump
  directly to that line in our .properties editor.
- Find Usages (Ctrl + Shift + G): Enable Eclipse's "Search for Usages" on a property key within the
  .properties editor to find everywhere that key is referenced in the Java source code.

2. Native Refactoring Support
   This is the most "pro" feature we could add:

- Rename Participant: If a user renames a property key in our editor, PropEditorX should participate in the
  Eclipse Refactoring engine to automatically update all string literal references in the Java code across
  the entire workspace.

3. Dual-Mode Editing (Source vs. Table)
   Many professional property editors provide a Table View tab alongside the Source View:

- Multi-Tab Editor: Add a "Design" or "Table" tab (using SWT Table or NatTable) that shows keys and values in
  a grid.
- Sync: Changes in the table view should immediately reflect in the source (with Unicode conversion) and vice
  versa. This makes managing large translation files much easier.

4. "I18n Intelligence" & Quick Fixes
   Since we already have a PropertyAnalyzer, we can expand it:

- Resource Bundle Validator: If messages.properties has a key that is missing in messages_ja.properties, mark
  it as a warning.
- Quick Fixes (Ctrl + 1): For the "Duplicate Key" marker we already implemented, add a Quick Fix to "Remove
  duplicate" or "Rename key" directly from the problems view.
- Placeholder Validation: Detect if a value uses {0}, {1} and warn if the Java code calling it passes the
  wrong number of arguments.

---

_Created on 2026-03-15_
