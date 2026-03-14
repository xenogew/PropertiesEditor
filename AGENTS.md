# Project Overview: PropEditorX

A specialized Java `.properties` file editor that supports automatic Unicode escape conversion (`\uXXXX` ↔ native characters), available as both an **Eclipse IDE plugin** and a **standalone SWT desktop application**.

- **Bundle Symbolic Name**: `io.github.xenogew.propedit`
- **Bundle Version**: 7.0.0.qualifier
- **Original Author**: Sou Miyazaki (Inspiration for PropEditorX)
- **Maintainer**: xenogew
- **Source**: Forked from [PropEditorX on OSDN](http://svn.osdn.net/svnroot/propedit/)
- **License**: See `LICENSE` file

---

## Current State (Modernized)

### Technologies

- **Java**: Targets **JavaSE-25**
- **Eclipse Platform**: Eclipse 2026-03 (Platform 4.39)
- **UI Toolkit**: **Eclipse SWT** (for both plugin and standalone)
- **Build System**: Maven + Eclipse Tycho 5.0.2
- **Performance**: Project Leyden (AppCDS + AOT Class Linking) for standalone app startup optimization
- **Localization**: English and Japanese (UTF-8 based)

---

## Modernization Roadmap

### Phase 1: Build System — Migrate to Maven + Eclipse Tycho ✅ DONE

### Phase 2: Java Version Upgrade ✅ DONE

### Phase 3: Java Language Modernization ✅ DONE

### Phase 4: Eclipse API Modernization ✅ DONE

### Phase 5: Swing Standalone App Modernization ✅ DONE

### Phase 6: Testing ✅ DONE

### Phase 7: Cleanup and Polish ✅ DONE

### Phase 8: Upgrade to Java 25, Eclipse 2026-03, and Project Leyden ✅ DONE

### Phase 9: Adopt Spotless for code formatting ✅ DONE

### Phase 10: Migrate Standalone Application from Swing to SWT ✅ DONE

### Phase 11: Rebranding and Namespace Refactoring ✅ DONE

### Phase 12: Make icons modern ✅ DONE

### Phase 13: Rebrand to PropEditorX and Implementation of New Logo ✅ DONE

**Goal**: Complete the final rebranding of the project to "PropEditorX", establishing a distinct modern identity while acknowledging the original inspiration from Sou Miyazaki. This includes implementing the new solar-flare-themed logo with size-optimized variations.

#### 13a. Logo Modernization ✅ DONE

- **Source**: Used `icons/PropEditorX-Icon-16x16.svg`, `32x32`, and `48x48` as sources for better clarity at small scales.
- **Rasterization**: Converted the SVGs into high-quality transparent PNGs tailored for each size.
  - `16x16`: Updated `icons/pe_16.png`, `icons/icon-prop-editor-x.png`, and `src/io/github/xenogew/propedit/resource/pe_16.png`.
  - `32x32`: Updated `icons/pe_32.png` and `src/io/github/xenogew/propedit/resource/pe_32.png`.
  - `48x48`: Added `icons/pe_48.png` for future high-DPI support.
- **Integration**: Updated all references. The new logo is now the main icon for both the plugin and standalone app.

#### 13b. Name Rebranding (PropEditorX → PropEditorX) ✅ DONE

- **UI Labels**: Updated `plugin.properties`, `plugin_ja.properties`, `lang.properties`, and `lang_ja.properties`.
- **Documentation**: Updated `README.md` and `GEMINI.md`.
- **Credits**: Preserved the attribution to Sou Miyazaki.

#### 13c. Structural Re-verification ✅ DONE

- Verified internal package/bundle IDs remain stable.
- Build pass with `./mvnw clean verify`. All tests passed.

### Phase 14: Marketplace Readiness and P2 Update Site ✅ DONE

**Goal**: Prepare PropEditorX for publishing on the Eclipse Marketplace by restructuring the project into a standard multi-module Tycho build, creating an Eclipse Feature, and generating a P2 Update Site (Repository).

#### 14a. Icon Naming Harmonization ✅ DONE
- Harmonized brand icon naming to use the preferred `icon-prop-editor-x` prefix.
- Updated all references in `plugin.xml`, `about.ini`, and code.
- Removed redundant legacy icon files.

#### 14b. Project Restructuring (Standard Tycho Layout) ✅ DONE
- Restructured the repository into a multi-module Maven project:
    - `/bundles/io.github.xenogew.propedit`: Core plugin code and resources.
    - `/features/io.github.xenogew.propedit.feature`: Eclipse Feature project.
    - `/releng/io.github.xenogew.propedit.repository`: Eclipse Repository project (P2 Update Site).
- Updated the root `pom.xml` to serve as the parent aggregator.

#### 14c. Final Branding and Vendor Update ✅ DONE
- Updated `Bundle-Vendor` to `Santa Soft` in all localization and manifest files.
- Renamed main classes and references to `PropEditorX`.

#### 14d. GitHub Actions CI/CD ✅ DONE
- Implemented `.github/workflows/build.yml` for automated build and P2 site deployment to `gh-pages`.

#### 14e. Documentation for Installation ✅ DONE
- Updated `README.md` with a clear "Installation" section pointing to the automated Update Site.

---

## Critical Files for Any Change

| File                              | Why                                                                    |
| --------------------------------- | ---------------------------------------------------------------------- |
| `META-INF/MANIFEST.MF`            | Bundle identity, dependencies, Java version, activator                 |
| `plugin.xml`                      | All Eclipse extension point declarations                               |
| `pom.xml`                         | Maven/Tycho build and Java version configuration                       |
| `PropertiesDocumentProvider.java` | Core load/save logic with Unicode conversion — the heart of the plugin |
| `PropEditorX.java`           | Main application entry point                                           |
| `EncodeChanger.java`              | Core Unicode ↔ escape conversion algorithm                             |
| `CheckAndMarkDuplicateKey.java`   | Duplicate key detection and marker creation                            |
| `PropEditorXPlugin.java`     | Plugin activator — singleton, resource bundle                          |
