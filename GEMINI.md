# PropEditorX — Quick Reference

A Java `.properties` file editor focusing on **human-readable UTF-8 text**. Eclipse plugin + standalone SWT app.

## Tech Stack

| Component      | Version / Tool                      |
| -------------- | ----------------------------------- |
| Java           | 25 (JavaSE-25)                      |
| Build          | Maven + Eclipse Tycho 5.0.2         |
| Eclipse Target | 2026-03                             |
| UI Toolkit     | Native SWT                          |
| Testing        | JUnit 5.11.4 + maven-surefire 3.5.2 |
| Packaging      | OSGi bundle (`eclipse-plugin`)      |

## Features

- **Human-Readable UTF-8** — Preserves native characters (like Japanese/Chinese) in files without escaping to `\uXXXX`.
- **Dual-Mode Editing** — (Planned) Multi-locale grid view for side-by-side translation.
- **Syntax Highlighting** — Comments, keys, separators, values.
- **Duplicate Key Detection** — Marks duplicate keys with Eclipse problem markers.
- **Code Folding** — Fold property sections, content outline support.
- **JDT Integration** — Hover, completion, and Ctrl+Click navigation from Java source to property keys.
- **Find Usages** — `Ctrl + Shift + G` from property editor to find all Java references.
- **Standalone SWT App** — Native look-and-feel, search/replace, encoding selection.
- **Localization** — English + Japanese.

## Build & Run

```bash
# Build + test
./mvnw clean verify

# Run standalone
java -cp "bundles/io.github.xenogew.propedit/target/classes:$(cat cp.txt)" io.github.xenogew.propedit.PropEditorX
```

## Implementations

- Always following coding style of "Google Coding Style" which is latest updated and compatible with modern features like Lambda expressions, Records, etc.
- **IMPORTANT** Always keep coding based on idea of Clean Code and SOLID principles.
- Always execute `./mvnw spotless:apply` before `build`, `package`, `verify` or `install` commands.
- Always execute `./mvnw clean verify` every times after making changes to the code.
- Always prefer to use `Virtual Thread` when found the heavy compute operations.
- In `try-catch` blocks, when catching exceptions, always use the specific exception type instead of `Exception` and log the exception with Eclipse Development Platform Logger or Java Native Logger or JUL.
- When naming the variables, parameters, constants, etc., always use descriptive names and **DO NOT** abbreviate them except for common abbreviations like `id`, `url`, `xml`, `tmp`, etc that mostly common for developers.
- Always run `./mvnw spotless:apply` before `build`, `package`, `verify` or `install` commands.
- Always make data object class using `record` keyword and adding "Builder" pattern when the properies or attributes are more or equals to 3.

## Key Entry Points

- **Standalone main**: `io.github.xenogew.propedit.PropEditorX`
- **Plugin activator**: `io.github.xenogew.propedit.eclipse.plugin.PropEditorXPlugin`
- **Editor implementation**: `io.github.xenogew.propedit.eclipse.plugin.editors.PropEditorX`
- **Load/Save logic**: `io.github.xenogew.propedit.eclipse.plugin.editors.PropertiesDocumentProvider`
