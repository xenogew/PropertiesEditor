# PropEditorX

A specialized Java `.properties` file editor focusing on **human-readable UTF-8 text**, available as both an **Eclipse IDE plugin** and a **standalone SWT desktop application**.

Originally created by Sou Miyazaki. Modernized and maintained by **Santa Soft**.
Forked from [PropEditorX on OSDN](http://svn.osdn.net/svnroot/propedit/).

## Features

- **Human-Readable UTF-8** — Keeps your property files readable. No more `\u8ca1` sequences; just clean, native text like `財`.
- **Dual-Mode Editing** — (In Development) Edit multiple locales side-by-side in a modern grid view.
- **Syntax Highlighting** — Partition-based coloring for comments (`#`, `!`), keys, separators (`=`, `:`), and values. Colors are configurable via preferences.
- **Duplicate Key Detection** — Scans for duplicate keys and marks them with Eclipse problem markers.
- **Code Folding** — Projection-based folding with collapse/expand all actions.
- **Content Outline** — Property keys displayed in the Eclipse Outline view.
- **JDT Integration** — Hover shows property values in Java source; completion proposals suggest property keys; Ctrl+Click navigation to keys.
- **Find Usages** — Search for all Java references of a property key directly from the editor.
- **Localization** — English and other locale languages support.
  - e.g. ja - Japanese, kr - Korean, zh - Chinese

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
[![Install](https://marketplace.eclipse.org/sites/all/modules/custom/marketplace/images/installbutton.png)](https://marketplace.eclipse.org/marketplace-client-intro?mpit=5921234) _(Marketplace ID pending)_

#### 2. P2 Update Site

1. Go to **Help > Install New Software...**
2. Click **Add...**
3. Enter the following URL: `https://xenogew.github.io/PropertiesEditor/`
4. Select **PropEditorX** and follow the prompts.

#### 3. Offline Installation (ZIP)

1. Go to the [Releases](https://github.com/xenogew/PropertiesEditor/releases) page.
2. Download the `io.github.xenogew.propedit.repository-*.zip` file.
3. In Eclipse, go to **Help > Install New Software...**
4. Click **Add...** then **Archive...**
5. Select the downloaded ZIP file, click **Add**, then select **PropEditorX** to install.

## Project Structure

PropEditorX uses a standard multi-module Tycho layout:

```

PropertiesEditor/
├── bundles/
│ └── io.github.xenogew.propedit/ # Core Plugin & Standalone App
│ ├── META-INF/MANIFEST.MF # OSGi manifest
│ ├── plugin.xml # Extension points
│ ├── src/ # Java source code
│ └── icons/ # Harmonized PNG icons
├── features/
│ └── io.github.xenogew.propedit.feature/ # Eclipse Feature definition
├── releng/
│ └── io.github.xenogew.propedit.repository/ # P2 Update Site generation
├── target-platform/ # Eclipse target platform (.target)
└── .github/workflows/ # CI/CD (Auto-build & Deploy)

```

## License

See the `LICENSE` file and `bundles/io.github.xenogew.propedit/license/` for details.
