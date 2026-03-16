# Codebase Concerns

**Analysis Date:** 2025-03-15

## Tech Debt

**Data Loss during Save:**
- Issue: `PropertyBundleModel.saveAll()` completely overwrites `.properties` files by manually constructing the file content from a `TreeMap`. This process strips all comments, custom formatting, and original file structure.
- Files: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertyBundleModel.java`
- Impact: Users lose documentation (comments) within their properties files whenever they save via the Grid editor.
- Fix approach: Use a proper properties parser that preserves comments (like Apache Commons Configuration or a custom line-by-line editor) instead of `java.util.Properties`.

**Incomplete Feature Stubs:**
- Issue: The `PropertyAnalyzer` class, intended for validating and analyzing properties, is currently a stub that returns `null`.
- Files: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/analyze/PropertyAnalyzer.java`
- Impact: Features relying on property analysis (like duplicate key detection or cross-file validation mentioned in `BACKLOG.md`) are likely non-functional or missing.
- Fix approach: Implement the analysis logic or integrate with existing Eclipse JDT/PDE analysis tools.

**Legacy Unicode Conversion Code:**
- Issue: Significant blocks of code for `\uXXXX` conversion are commented out but remain in the codebase.
- Files: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertiesDocumentProvider.java`
- Impact: Increased maintenance burden and confusion for future developers.
- Fix approach: Completely remove the commented-out legacy logic once the transition to UTF-8 is fully verified and stable.

## Performance Bottlenecks

**Heavy Grid Refresh:**
- Issue: `PropertiesGridPage.refresh()` disposes and re-creates every SWT control in the grid whenever a change is made or a save occurs.
- Files: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertiesGridPage.java`
- Cause: The implementation lacks a granular update mechanism. For large properties files, this "nuclear" refresh approach will cause significant UI lag and flickering.
- Improvement path: Implement a data-binding mechanism or manually update only the affected `TableItem` and its associated `TableEditor` controls.

**Synchronous File Discovery:**
- Issue: `PropertyBundleModel.discoverFiles()` performs a synchronous `container.refreshLocal` and iterates over all files in the directory.
- Files: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertyBundleModel.java`
- Cause: Occurs during editor initialization and refresh. In large workspaces with many files, this can block the UI thread.
- Improvement path: Offload file discovery to a background `Job` or use the Eclipse Indexer/Resource delta system.

## Fragile Areas

**Theme Incompatibility (Hardcoded Colors):**
- Files: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertiesGridPage.java`
- Why fragile: Hardcoded RGB values (e.g., `RGB(148, 163, 184)`) do not respect Eclipse's Dark/Light themes. This will result in poor readability (e.g., black text on dark gray) for users with dark themes.
- Safe modification: Use `JFaceResources.getColorRegistry()` or `IFormColors` from the `FormToolkit` to fetch theme-aware colors.

**Manual UI Layout and Sizing:**
- Files: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertiesGridPage.java`
- Why fragile: The editor uses a manual `rowHeight` calculation and hardcoded column width ratios (0.3/0.7). It also relies on `asyncExec` to wait for UI dimensions before populating the table. This is prone to race conditions and layout breaks on high-DPI displays or different OS font settings.
- Safe modification: Use standard SWT layout managers more effectively and rely on `TableColumn` weights or `AutoResize` behaviors.

**Debug Artifacts in Production:**
- Files: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertiesGridPage.java`
- Why fragile: `PaintListener`s for drawing "Debug Borders" are active in the production code path.
- Safe modification: Wrap these in a debug flag or remove them entirely.

## Scaling Limits

**Memory Consumption (In-Memory Bundles):**
- Current capacity: Entire property bundles (multiple locale files) are loaded into memory as `Properties` objects.
- Limit: For projects with thousands of properties and dozens of locales, this could lead to significant memory overhead.
- Scaling path: Implement a lazy-loading mechanism or a more memory-efficient data structure for property storage.

## Test Coverage Gaps

**Grid Editor Validation:**
- What's not tested: The synchronization logic between the Source tab and Grid tab during save operations is complex and currently untested.
- Files: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropEditorX.java`
- Risk: Concurrent edits in both Source and Grid tabs could lead to data loss or corruption if the `doSave` sequence fails or races.
- Priority: High

**Multi-Locale Model:**
- What's not tested: The `PropertyBundleModel`'s ability to discover and correctly merge keys from multiple locale files.
- Files: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertyBundleModel.java`
- Risk: Incorrect locale identification or key collisions.
- Priority: Medium

---

*Concerns audit: 2025-03-15*
