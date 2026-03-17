---
phase: 01-grid-performance
plan: 01
type: execute
wave: 1
depends_on: []
files_modified:
  - target-platform/target-platform.target
  - bundles/io.github.xenogew.propedit/META-INF/MANIFEST.MF
  - bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertyBundleModel.java
  - bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertiesGridViewModel.java
autonomous: true
requirements: [REQ-1.6, REQ-1.7]
must_haves:
  truths:
    - "Project compiles with Nebula NatTable dependencies"
    - "PropertyBundleModel provides data via ISpanningDataProvider interface"
    - "PropertiesGridViewModel is implemented to decouple View from Model"
  artifacts:
    - path: "bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertyBundleModel.java"
      provides: "ISpanningDataProvider implementation"
    - path: "bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertiesGridViewModel.java"
      provides: "ViewModel for the grid"
key_links:
  - from: "bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertyBundleModel.java"
    to: ".planning/milestone-1/phase-1/VALIDATION.md"
    role: "feeds data to virtualized layers"
---

<objective>
Establish the infrastructure and data model required for Nebula NatTable integration.
</objective>

<tasks>
<task type="auto">
  <name>Task 1: Update Target Platform and Manifest</name>
  <files>target-platform/target-platform.target, bundles/io.github.xenogew.propedit/META-INF/MANIFEST.MF</files>
  <action>
    - Add Nebula NatTable update site (https://download.eclipse.org/nattable/releases/2.1.0/repository/) to target-platform.target.
    - Include units: org.eclipse.nebula.widgets.nattable.core, org.eclipse.nebula.widgets.nattable.extension.e4.
    - Update MANIFEST.MF Require-Bundle to include these units.
  </action>
  <verify>
    <automated>./mvnw clean compile -DskipTests</automated>
  </verify>
  <done>Dependencies are available in the classpath.</done>
</task>

<task type="auto">
  <name>Task 2: Refactor PropertyBundleModel for NatTable</name>
  <files>bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertyBundleModel.java</files>
  <action>
    - Refactor PropertyBundleModel to implement org.eclipse.nebula.widgets.nattable.data.ISpanningDataProvider.
    - Ensure row/column mapping supports spanning for the Key column.
  </action>
  <verify>
    <automated>./mvnw test -Dtest=PropertyBundleModelTest</automated>
  </verify>
  <done>Model is ready to feed data to NatTable layers.</done>
</task>

<task type="auto">
  <name>Task 3: Implement PropertiesGridViewModel</name>
  <files>bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertiesGridViewModel.java</files>
  <action>
    - Create the ViewModel to bridge between PropertyBundleModel and the Grid View.
    - Implement logic for row/column indexing, cell value retrieval, and update debouncing.
  </action>
  <verify>
    <automated>Verify file exists and compiles.</automated>
  </verify>
  <done>ViewModel is ready to be used by the Grid Page.</done>
</task>
</tasks>
