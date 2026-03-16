# Modern Java Properties Management (Research Results)

**Domain:** Java Properties Ecosystem
**Date:** 2025-03-15
**Focus:** UTF-8 Support, Comment Preservation, Sorting Strategies.

---

## 1. UTF-8 Support (Java 9+)

Historically, Java `.properties` files were strictly **ISO-8859-1**. Non-ASCII characters had to be escaped using `\uXXXX`.

### Current State (Java 9 to 25):
*   **JEP 226:** Introduced in Java 9, this changed the default encoding for `PropertyResourceBundle` to **UTF-8**.
*   **Fallback:** If a file contains invalid UTF-8 sequences, the runtime falls back to ISO-8859-1.
*   **The Trap:** The `java.util.Properties` class still defaults to ISO-8859-1 when using `load(InputStream)`. To use UTF-8 reliably, you **must** use `load(Reader)` with a UTF-8 reader.

### Recommendation for PropEditorX:
*   Standardize on **UTF-8**.
*   Avoid `\uXXXX` escapes in the editor view to keep files human-readable.
*   Use `StandardCharsets.UTF_8` explicitly in all File I/O operations.

---

## 2. Preservation of Comments and Formatting

The standard `java.util.Properties` class is a "Map" that discards everything except keys and values when loaded and written.

### Best-in-Class: Apache Commons Configuration 2
The `PropertiesConfiguration` class, combined with `PropertiesConfigurationLayout`, is the industry standard for "round-trip" editing.

*   **Capabilities:**
    *   Tracks the position of every comment.
    *   Preserves blank lines.
    *   Maintains the original order of keys (if desired).
    *   Handles multi-line values correctly.
*   **Integration:** Since the project is an Eclipse Plugin (OSGi), this library is typically available as a bundle or can be embedded.

### Lightweight Alternative: mizosoft/java-properties
A focused library that treats comments as nodes. Good if a small footprint is required.

---

## 3. Robust Sorting Strategies

Sorting is critical for "Git-friendliness" (reducing merge conflicts).

### Recommended Strategies:
1.  **Strict Alphabetical:** Sorts all keys A-Z. Best for CI/CD and large teams.
2.  **Logical Grouping:** Sorts by prefix (e.g., `mail.host`, `mail.port`, `server.url`). This keeps related settings together.
3.  **Structure-Aware Sorting:** This is the "Holy Grail." It moves the comment block *with* the property. 
    *   *Implementation Tip:* Treat the file as a list of "Blocks" (Comment + Key-Value). Sort the blocks by the key.

### Tooling for Enforcement:
*   **Spotless:** A modern build tool plugin that can enforce properties sorting during the Maven/Gradle build.
*   **proptidy:** A legacy but still functional tool for cleaning up properties files.

---

## 4. Modern Tooling (2024-2025)

*   **Linter:** Modern properties linters (like those in SonarQube) now check for:
    *   Duplicate keys (which `Properties.load` silently ignores by overwriting).
    *   Inconsistent naming (kebab-case vs camelCase).
    *   Hardcoded secrets (API keys).
*   **IDE Support:** IntelliJ and Eclipse have "Resource Bundle" views that allow side-by-side editing of multiple locales, which PropEditorX should aim to match or exceed in UX.

---

## Summary for Roadmap

1.  **Primary Engine:** Apache Commons Configuration 2 (for layout preservation).
2.  **Default Encoding:** UTF-8.
3.  **Key Feature:** "Sort with Comments" (Structure-aware sorting).
4.  **UX Goal:** Human-readable editing without mandatory `\uXXXX` escaping.
