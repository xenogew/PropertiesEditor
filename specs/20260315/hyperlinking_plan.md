# Phase 15: Java to Properties Hyperlinking

**Goal**: Enable `Ctrl + Click` navigation from a property key in a Java string literal directly to the corresponding key in the `.properties` editor.

## Technical Design

### 1. Hyperlink Detector Implementation
Create a new class `io.github.xenogew.propedit.eclipse.plugin.jdt.hyperlink.PropertiesHyperlinkDetector`.
- It will implement `org.eclipse.jface.text.hyperlink.IHyperlinkDetector`.
- It will reuse the logic from `PropertiesHover` to identify the string literal at the current cursor offset.
- It will use `ProjectProperties.getInstance().getProperty(project, targetKey)` to find the files containing the key.

### 2. Hyperlink Implementation
Create a new class `io.github.xenogew.propedit.eclipse.plugin.jdt.hyperlink.PropertiesHyperlink`.
- It will implement `org.eclipse.jface.text.hyperlink.IHyperlink`.
- `open()` method will:
  1. Find the `IFile` associated with the key.
  2. Open the file in the `PropEditorX` editor.
  3. Search for the key in the document and set the cursor selection to that line.

### 3. Extension Point Registration
Register the detector in `plugin.xml`:
```xml
<extension point="org.eclipse.ui.workbench.texteditor.hyperlinkDetectors">
    <hyperlinkDetector
        id="io.github.xenogew.propedit.eclipse.plugin.jdt.hyperlink.PropertiesHyperlinkDetector"
        class="io.github.xenogew.propedit.eclipse.plugin.jdt.hyperlink.PropertiesHyperlinkDetector"
        name="PropEditorX Hyperlink Detector"
        targetId="org.eclipse.jdt.ui.javaCode">
    </hyperlinkDetector>
</extension>
```

## Implementation Steps

1. **Research Utility**: Move the string-literal detection logic from `PropertiesHover` into a utility class (e.g., `JdtUtil.java`) to avoid duplication.
2. **Implement Detector**: Create `PropertiesHyperlinkDetector`.
3. **Implement Hyperlink**: Create `PropertiesHyperlink`.
4. **Register Extension**: Update `plugin.xml`.
5. **Validation**: Test by `Ctrl + Clicking` a property key in a Java file within an Eclipse workspace.

## Notes

- Always prefer to use `Virtual Thread` when found the heavy compute operations.

---
*Created on 2026-03-15*
