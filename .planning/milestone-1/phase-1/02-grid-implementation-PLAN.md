---
phase: 01-grid-performance
plan: 02
type: execute
wave: 2
depends_on: ["01-grid-performance-01"]
files_modified:
  - bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertiesGridPage.java
autonomous: true
requirements: [REQ-1.6, REQ-1.7]
must_haves:
  truths:
    - "Grid uses Nebula NatTable instead of SWT Table"
    - "SpanningDataLayer is configured for the Key column"
    - "e4 CSS styling is applied to NatTable"
  artifacts:
    - path: "bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertiesGridPage.java"
      provides: "Virtualized NatTable implementation"
key_links:
  - from: "bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertiesGridPage.java"
    to: ".planning/milestone-1/phase-1/VALIDATION.md"
    role: "visual verification of virtualization and styling"
---

<objective>
Rebuild PropertiesGridPage using NatTable with virtualization and cell spanning.
</objective>

<tasks>
<task type="auto">
  <name>Task 1: Rebuild Grid with NatTable Layer Stack</name>
  <files>bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertiesGridPage.java</files>
  <action>
    - Replace SWT Table with NatTable.
    - Implement Layer Stack: DataLayer -> SpanningDataLayer -> SelectionLayer -> ViewportLayer.
    - Configure SpanningDataLayer for the 'Property Key' column.
    - Implement pl-2 (10px) horizontal padding for all cells.
  </action>
  <verify>
    <automated>./mvnw clean compile -DskipTests</automated>
  </verify>
  <done>Grid displays data correctly in a virtualized manner.</done>
</task>

<task type="auto">
  <name>Task 2: Implement Styling and Flyweight Editor</name>
  <files>bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertiesGridPage.java</files>
  <action>
    - Integrate NatTable e4 extension for CSS.
    - Define CSS selectors for Slate-400 (#94a3b8) headers and gray grid lines (#e2e8f0).
    - Configure EditLayer with a TextCellEditor for the 'Value' column (Flyweight).
    - Set single-click activation for the editor.
  </action>
  <verify>
    <automated>Check for presence of NatTable e4 integration code</automated>
  </verify>
  <done>Grid looks professional and supports flyweight editing.</done>
</task>
</tasks>
