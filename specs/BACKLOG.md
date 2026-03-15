# PropEditorX Improvements Backlog

These are the proposed ideas to enhance the Eclipse Plugin functionality, kept as our source reference.

| ID | Feature Area | Detailed Description | Status |
| :--- | :--- | :--- | :--- |
| 1 | **Bidirectional Navigation** | **JDT Integration**: Make it truly "IDE-native":<br>• **Hyperlinking (Ctrl + Click)**: Jump from Java string literal to .properties editor.<br>• **Find Usages (Ctrl + Shift + G)**: Search for property key references in Java source code from the properties editor. | **Done** |
| 2 | **Native Refactoring Support** | **Rename Participant**: Participate in the Eclipse Refactoring engine. Automatically update all string literal references in Java code across the workspace when a property key is renamed in our editor. | Backlog |
| 3 | **Dual-Mode Editing** | **Source vs. Table**: Add a "Design" or "Table" tab (using SWT Table or NatTable) alongside the Source view. Changes should sync bi-directionally with automatic Unicode conversion. | Backlog |
| 4 | **I18n Intelligence & Quick Fixes** | **Expanded PropertyAnalyzer**:<br>• **Resource Bundle Validator**: Detect missing keys across translation files (e.g., English vs Japanese).<br>• **Quick Fixes (Ctrl + 1)**: One-click "Remove duplicate" or "Rename key" from the Problems view.<br>• **Placeholder Validation**: Verify `{0}, {1}` usage against Java calls. | Backlog |

---
_Created on 2026-03-15_
