# Coding Conventions

**Analysis Date:** 2025-03-15

## Naming Patterns

**Files:**
- Java source files use `PascalCase`: `PropertiesSourceEditor.java`, `StringUtil.java`.
- Test files suffix with `Test`: `StringUtilTest.java`.
- Plugin metadata: `plugin.properties`, `plugin.xml`.

**Functions:**
- Use `camelCase`: `removeCarriageReturn()`, `updateFoldingStructure()`.
- Test methods often use descriptive names: `removesWindowsLineEndings()`, `escapedSeparatorInKey()`.

**Variables:**
- Use `camelCase`: `colorManager`, `annotationModel`.
- Some legacy-style prefixes observed (e.g., `fOutlinePage`, `fLineNumberRulerColumn`).

**Types:**
- Classes and Interfaces use `PascalCase`: `ColorManager`, `IContentOutlinePage`.

## Code Style

**Formatting:**
- **Tool:** Spotless Maven Plugin (`com.diffplug.spotless:spotless-maven-plugin`).
- **Configuration:** Eclipse Java Google Style (`eclipse-java-google-style.xml`).
- **Settings:** 2-space indentation, 100-character line length, imports ordered and unused imports removed.

**Linting:**
- Handled via `spotless:check` during the `compile` phase.
- Java Version: 25.

## Import Organization

**Order:**
- Ordered alphabetically by Spotless.
- Static imports (like JUnit assertions) typically appear first.

**Path Aliases:**
- Not applicable (standard Java/OSGi bundle structure).

## Error Handling

**Patterns:**
- Standard Java try-catch blocks.
- Eclipse `CoreException` for plugin-specific errors.
- Extensive use of `@SuppressWarnings("unchecked")` for Eclipse adapter patterns.

## Logging

**Framework:** Eclipse logging via `PropEditorXPlugin`.

**Patterns:**
- Use `PropEditorXPlugin.getDefault().getLog().log(...)` or standard Eclipse error reporting.

## Comments

**When to Comment:**
- Classes often have Javadoc headers describing their purpose.
- Non-obvious logic or Eclipse API overrides are occasionally commented.

**JSDoc/TSDoc:**
- Standard Javadoc is used: `/** ... */`.

## Function Design

**Size:** Generally focused, but some UI-related methods (like `updateFoldingStructure` in `PropertiesSourceEditor.java`) can be complex.

**Parameters:** Standard Java parameters, often passing Eclipse context objects like `IProgressMonitor`.

**Return Values:** Explicit types; use of `Optional` not yet prevalent in observed files despite Java 25.

## Module Design

**Exports:**
- Controlled via `META-INF/MANIFEST.MF` (standard OSGi/Eclipse Plugin pattern).

**Barrel Files:**
- Not applicable (Java packages used instead).

---

*Convention analysis: 2025-03-15*
