# PropEditorX Improvements Backlog

These are the proposed ideas to enhance the Eclipse Plugin functionality, kept as our source reference.

| ID | Feature Area | Detailed Description | Status |
| :--- | :--- | :--- | :--- |
| 1 | **Bidirectional Navigation** | **JDT Integration**: Make it truly "IDE-native":<br>• **Hyperlinking (Ctrl + Click)**: Jump from Java string literal to .properties editor.<br>• **Find Usages (Ctrl + Shift + G)**: Search for property key references in Java source code from the properties editor. | **Done** |
| 2 | **Native Refactoring Support** | **Rename Participant**: Participate in the Eclipse Refactoring engine. Automatically update all string literal references in Java code across the workspace when a property key is renamed in our editor. | Backlog |
| 3 | **Dual-Mode Editing** | **Source vs. Table**: Add a "Design" or "Table" tab (using SWT Table or NatTable) alongside the Source view. Changes should sync bi-directionally with automatic Unicode conversion. | Backlog |
| 4 | **I18n Intelligence & Quick Fixes** | **Expanded PropertyAnalyzer**:<br>• **Resource Bundle Validator**: Detect missing keys across translation files (e.g., English vs Japanese).<br>• **Quick Fixes (Ctrl + 1)**: One-click "Remove duplicate" or "Rename key" from the Problems view.<br>• **Placeholder Validation**: Verify `{0}, {1}` usage against Java calls. | Backlog |

---

## Technical Research Notes: Virtual Threads in Eclipse/SWT

Research conducted on 2026-03-15 regarding the use of Java 25 Virtual Threads within the Eclipse Plugin architecture.

### 1. The "Bridge" Pattern
SWT is strictly single-threaded. To use Virtual Threads for background logic, we must bridge back to the UI thread for any workbench operations:
```java
Thread.ofVirtual().start(() -> {
    // 1. Logic/IO (Virtual Thread)
    var result = heavyProcess(); 
    // 2. Bridge back to UI (SWT Thread)
    Display.getDefault().asyncExec(() -> {
        updateUI(result);
    });
});
```

### 2. Compatibility with Eclipse APIs
- **Search API**: Methods like `NewSearchUI.runQueryInBackground()` trigger UI views internally and **must** be initiated from the UI Thread. Since they already spawn their own background Jobs, wrapping them in a Virtual Thread provides no performance benefit and triggers `SWTException`.
- **Ideal Use Cases**: Virtual Threads are highly recommended for future features involving massive workspace I/O, such as **Cross-file Key Validation** (checking hundreds of properties files simultaneously).

### 3. Future Roadmap
The Eclipse Platform is currently exploring native integration of Virtual Threads into the `Job` system (Eclipse 2026+), aligning with our modernized tech stack.

---
_Created on 2026-03-15_
