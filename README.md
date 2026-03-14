# PropEditorX

A specialized Java `.properties` file editor with automatic Unicode escape conversion (`\uXXXX` ↔ native characters), available as both an **Eclipse IDE plugin** and a **standalone Swing/SWT desktop application**.

Originally created by Sou Miyazaki. Modernized and maintained by xenogew.
Forked from [PropEditorX on OSDN](http://svn.osdn.net/svnroot/propedit/).

## Features

- **Unicode Escape Conversion** — Automatically converts `\uXXXX` → native characters on load, and native → `\uXXXX` on save. Supports upper/lowercase hex and an option to skip comments.
- **Syntax Highlighting** — Partition-based coloring for comments (`#`, `!`), keys, separators (`=`, `:`), and values. Colors are configurable via preferences.
- **Duplicate Key Detection** — Scans for duplicate keys and marks them with Eclipse problem markers.
- **Code Folding** — Projection-based folding with collapse/expand all actions.
- **Content Outline** — Property keys displayed in the Eclipse Outline view.
- **JDT Integration** — Hover shows property values in Java source; completion proposals suggest property keys inside string literals.
- **Standalone App** — Full desktop application with file open/save, search/replace, encoding selection, and Unicode conversion.
- **Localization** — English and Japanese.

## Prerequisites

- **Java 21+** — JDK 21+ with `JAVA_HOME` set
- **Maven** — Included via Maven Wrapper (`./mvnw`)

## Build

```bash
./mvnw clean verify
```

This compiles the plugin, runs 51 unit tests, and packages the OSGi bundle JAR in `target/`.

## Installation

### Eclipse IDE

You can install PropEditorX into your Eclipse IDE using the following methods:

#### 1. Drag and Drop
Drag the following button into your running Eclipse workspace:
[![Install](https://marketplace.eclipse.org/sites/all/modules/custom/marketplace/images/installbutton.png)](https://marketplace.eclipse.org/marketplace-client-intro?mpit=5921234) *(Marketplace ID pending)*

#### 2. P2 Update Site
1. Go to **Help > Install New Software...**
2. Click **Add...**
3. Enter the following URL: `https://xenogew.github.io/PropEditorX/`
4. Select **PropEditorX** and follow the prompts.

### Standalone App

```bash
java -cp "target/classes:$(cat cp.txt)" io.github.xenogew.propedit.PropEditorX
```


### Optimization (Project Leyden)

For near-instant startup on Java 25+, you can generate an optimized CDS archive:

```bash
./build-leyden.sh
```

Then run with optimization flags:

```bash
java -XX:SharedArchiveFile=properties-editor.jsa -XX:+AOTClassLinking -cp "target/classes:$(cat cp.txt)" io.github.xenogew.propedit.PropEditorX
```

## Running as Eclipse Plugin

Install the built JAR (`target/io.github.xenogew.propedit-7.0.0-SNAPSHOT.jar`) into your Eclipse `dropins/` folder, or use a P2 update site.

## Project Structure

```
PropEditorX/
├── META-INF/MANIFEST.MF          # OSGi bundle manifest
├── plugin.xml                     # Eclipse extension point declarations
├── build.properties               # PDE build configuration
├── pom.xml                        # Maven/Tycho build
├── target-platform/               # Eclipse target platform definition
├── lib/flatlaf-3.6.jar            # FlatLaf look-and-feel
├── icons/                         # Eclipse plugin icons (PNG)
├── schema/                        # Extension point schemas
├── src/
│   ├── io/github/xenogew/propedit/
│   │   ├── eclipse/plugin/        # Eclipse plugin code
│   │   ├── util/                  # Core utilities (EncodeChanger, etc.)
│   │   ├── bean/                  # Data classes
│   │   ├── swing/                 # Swing utility classes
│   │   ├── io/                    # Custom I/O classes
│   │   └── ...                    # Standalone App classes
│   └── test/java/                 # JUnit 5 unit tests
└── license/                       # License files
```

## License

See the [LICENSE](license/) file for details.
