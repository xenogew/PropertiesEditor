# PropEditorX

A specialized Java `.properties` file editor with automatic Unicode escape conversion (`\uXXXX` ↔ native characters), available as both an **Eclipse IDE plugin** and a **standalone SWT desktop application**.

Originally created by Sou Miyazaki. Modernized and maintained by **Santa Soft**.
Forked from [PropEditorX on OSDN](http://svn.osdn.net/svnroot/propedit/).

## Features

- **Unicode Escape Conversion** — Automatically converts `\uXXXX` → native characters on load, and native → `\uXXXX` on save. Supports upper/lowercase hex and an option to skip comments.
- **Syntax Highlighting** — Partition-based coloring for comments (`#`, `!`), keys, separators (`=`, `:`), and values. Colors are configurable via preferences.
- **Duplicate Key Detection** — Scans for duplicate keys and marks them with Eclipse problem markers.
- **Code Folding** — Projection-based folding with collapse/expand all actions.
- **Content Outline** — Property keys displayed in the Eclipse Outline view.
- **JDT Integration** — Hover shows property values in Java source; completion proposals suggest property keys inside string literals.
- **Standalone App** — Full native SWT desktop application with file open/save, search/replace, encoding selection, and Unicode conversion.
- **Localization** — English and Japanese (UTF-8 based).

## Prerequisites

- **Java 25+** — JDK 25+ with `JAVA_HOME` set
- **Maven** — Included via Maven Wrapper (`./mvnw`)

## Build

```bash
./mvnw clean verify
```

This compiles the project, runs 53 unit tests, and packages the modules (Plugin, Feature, and P2 Repository).

## Installation

### Eclipse IDE

You can install PropEditorX into your Eclipse IDE using the following methods:

#### 1. Drag and Drop
Drag the following button into your running Eclipse workspace:
[![Install](https://marketplace.eclipse.org/sites/all/modules/custom/marketplace/images/installbutton.png)](https://marketplace.eclipse.org/marketplace-client-intro?mpit=5921234) *(Marketplace ID pending)*

#### 2. P2 Update Site
1. Go to **Help > Install New Software...**
2. Click **Add...**
3. Enter the following URL: `https://xenogew.github.io/PropertiesEditor/`
4. Select **PropEditorX** and follow the prompts.

### Standalone App

```bash
# Run using the built JAR and its dependencies
java -cp "bundles/io.github.xenogew.propedit/target/classes:$(cat cp.txt)" io.github.xenogew.propedit.PropEditorX
```

### Optimization (Project Leyden)

For near-instant startup on Java 25+, you can generate an optimized AppCDS archive:

```bash
./build-leyden.sh
```

Then run with AOT optimization flags:

```bash
java -XX:SharedArchiveFile=properties-editor.jsa -XX:+AOTClassLinking -cp "bundles/io.github.xenogew.propedit/target/classes:$(cat cp.txt)" io.github.xenogew.propedit.PropEditorX
```

## Project Structure

PropEditorX uses a standard multi-module Tycho layout:

```
PropertiesEditor/
├── bundles/
│   └── io.github.xenogew.propedit/    # Core Plugin & Standalone App
│       ├── META-INF/MANIFEST.MF      # OSGi manifest
│       ├── plugin.xml                 # Extension points
│       ├── src/                       # Java source code
│       └── icons/                     # Harmonized PNG icons
├── features/
│   └── io.github.xenogew.propedit.feature/ # Eclipse Feature definition
├── releng/
│   └── io.github.xenogew.propedit.repository/ # P2 Update Site generation
├── target-platform/                   # Eclipse target platform (.target)
└── .github/workflows/                 # CI/CD (Auto-build & Deploy)
```

## License

See the `LICENSE` file and `bundles/io.github.xenogew.propedit/license/` for details.
