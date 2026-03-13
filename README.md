# PropertiesEditor

A specialized Java `.properties` file editor with automatic Unicode escape conversion (`\uXXXX` ↔ native characters), available as both an **Eclipse IDE plugin** and a **standalone Swing desktop application**.

Forked from [PropertiesEditor on OSDN](http://svn.osdn.net/svnroot/propedit/) by Sou Miyazaki, modernized to Java 25.

## Features

- **Unicode Escape Conversion** — Automatically converts `\uXXXX` → native characters on load, and native → `\uXXXX` on save. Supports upper/lowercase hex and an option to skip comments.
- **Syntax Highlighting** — Partition-based coloring for comments (`#`, `!`), keys, separators (`=`, `:`), and values. Colors are configurable via preferences.
- **Duplicate Key Detection** — Scans for duplicate keys and marks them with Eclipse problem markers.
- **Code Folding** — Projection-based folding with collapse/expand all actions.
- **Content Outline** — Property keys displayed in the Eclipse Outline view.
- **JDT Integration** — Hover shows property values in Java source; completion proposals suggest property keys inside string literals.
- **Standalone Swing App** — Full desktop application with FlatLaf look-and-feel, file open/save, search/replace, encoding selection, and Unicode conversion.
- **Localization** — English and Japanese.

## Prerequisites

- **Java 25** — JDK 25 with `JAVA_HOME` set
- **Maven** — Included via Maven Wrapper (`./mvnw`)

## Build

```bash
./mvnw clean verify
```

This compiles the plugin, runs 51 unit tests, and packages the OSGi bundle JAR in `target/`.

## Running Standalone

```bash
java -cp "target/classes:lib/flatlaf-3.6.jar" jp.gr.java_conf.ussiy.app.propedit.PropertiesEditor
```

Or run `PropertiesEditor.main()` from your IDE.

## Running as Eclipse Plugin

Install the built JAR (`target/jp.gr.java_conf.ussiy.app.propedit-7.0.0-SNAPSHOT.jar`) into your Eclipse `dropins/` folder, or use a P2 update site.

## Project Structure

```
PropertiesEditor/
├── META-INF/MANIFEST.MF          # OSGi bundle manifest
├── plugin.xml                     # Eclipse extension point declarations
├── build.properties               # PDE build configuration
├── pom.xml                        # Maven/Tycho build
├── target-platform/               # Eclipse target platform definition
├── lib/flatlaf-3.6.jar            # FlatLaf look-and-feel
├── icons/                         # Eclipse plugin icons (GIF)
├── schema/                        # Extension point schemas
├── src/
│   ├── jp/gr/java_conf/ussiy/
│   │   ├── app/propedit/          # Standalone Swing app + shared utilities
│   │   │   ├── eclipse/plugin/    # Eclipse plugin code
│   │   │   ├── util/              # Core utilities (EncodeChanger, etc.)
│   │   │   └── bean/              # Data classes
│   │   ├── swing/                 # Swing utility classes
│   │   └── util/                  # General utilities (StringUtil)
│   └── test/java/                 # JUnit 5 unit tests
└── license/                       # License files
```

## License

See the [LICENSE](license/) file for details.
