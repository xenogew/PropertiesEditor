# PropEditorX — Quick Reference

A Java `.properties` file editor with automatic `\uXXXX` ↔ native character conversion. Eclipse plugin + standalone Swing app.

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
- Standalone Swing app with FlatLaf, search/replace, encoding selection
- **Modernized Icons** — All 26 GIF icons converted to PNG; visual directory available at `icon_directory.png`
- English + Japanese localization

## Build & Run

```bash
# Build + test (51 unit tests)
./mvnw clean verify

# Run standalone
java -cp "target/classes:lib/flatlaf-3.6.jar" io.github.xenogew.propedit.PropEditorX
```

## Key Entry Points

- **Standalone main**: `io.github.xenogew.propedit.PropEditorX`
- **Plugin activator**: `io.github.xenogew.propedit.eclipse.plugin.PropEditorXPlugin`
- **Core conversion**: `io.github.xenogew.propedit.util.EncodeChanger`
