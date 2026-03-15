# PropEditorX — Quick Reference

A Java `.properties` file editor as Eclipse plugin.

## Tech Stack

| Component      | Version / Tool                      |
| -------------- | ----------------------------------- |
| Java           | 25 (JavaSE-25)                      |
| Build          | Maven + Eclipse Tycho 5.0.2         |
| Eclipse Target | 2024-12                             |
| Look & Feel    | FlatLaf 3.6 (Swing app)             |
| Testing        | JUnit 5.11.4 + maven-surefire 3.5.2 |
| Packaging      | OSGi bundle (`eclipse-plugin`)      |

## Features

- Unicode escape conversion (auto on load/save, configurable case, comment-aware)
- Syntax highlighting (comments, keys, separators, values)
- Duplicate key detection with Eclipse problem markers
- Code folding, content outline
- JDT hover + completion proposals for property keys in Java source
- **Modernized Icons** — All 26 GIF icons converted to PNG; visual directory available at `icon_directory.png`
- English + Japanese + etc localization

## Build & Run

```bash
# Build + test (53 unit tests)
./mvnw clean verify
```

## Implementations

- Always following coding style of "Google Coding Style" which is latest updated and compatible with modern features like Lambda expressions, Records, etc.
- Always prefer to use `Virtual Thread` when found the heavy compute operations.
- In `try-catch` blocks, when catching exceptions, always use the specific exception type instead of `Exception` and log the exception with Eclipse Development Platform Logger or Java Native Logger or JUL.
- When naming the variables, parameters, constants, etc., always use descriptive names and **DO NOT** abbreviate them except for common abbreviations like `id`, `url`, `xml`, `tmp`, etc that mostly common for developers.
- Always run `./mvnw spotless:apply` before `build`, `package`, `verify` or `install` commands.

## Key Entry Points

- **Standalone main**: `io.github.xenogew.propedit.PropEditorX`
- **Plugin activator**: `io.github.xenogew.propedit.eclipse.plugin.PropEditorXPlugin`
- **Core conversion**: `io.github.xenogew.propedit.util.EncodeChanger`
