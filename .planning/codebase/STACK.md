# Technology Stack

**Analysis Date:** 2025-03-01

## Languages

**Primary:**
- Java 25 - Core language for both Eclipse plugin and standalone application (`pom.xml`, `MANIFEST.MF`, `build-leyden.sh`).

**Secondary:**
- XML - Plugin configuration (`plugin.xml`), build configuration (`pom.xml`), and target platform definition (`target-platform.target`).
- Shell Script - Build and optimization scripts (`build-leyden.sh`, `mvnw`).

## Runtime

**Environment:**
- JRE 25 - Required for runtime execution, including specific flags for native access (`--enable-native-access=ALL-UNNAMED` for SWT).
- OSGi (Eclipse) - Plugin runtime environment for integration with the Eclipse IDE.
- SWT (Standard Widget Toolkit) - GUI toolkit used for both the IDE plugin and the standalone desktop application.

**Package Manager:**
- Maven 3.x - Used for project management and dependency resolution.
- Maven Wrapper (`mvnw`) - Ensures consistent Maven version usage.
- Lockfile: `skills-lock.json` (not a standard Maven lockfile, but present in root).

## Frameworks

**Core:**
- Eclipse Tycho 5.0.2 - Maven extension for building Eclipse plugins and managing target platforms (`pom.xml`).
- Eclipse Platform SDK - Provides the core plugin architecture and extension points (`plugin.xml`).

**Testing:**
- JUnit 5 (5.11.4) - Used for unit testing (`pom.xml`).

**Build/Dev:**
- Spotless 2.44.2 - Code formatting and linting (Google Java Format mentioned in root `pom.xml`).
- Project Leyden / AppCDS - Used for startup optimization in the standalone application (`build-leyden.sh`).

## Key Dependencies

**Critical:**
- `org.eclipse.platform.feature.group` - Core Eclipse platform features (`target-platform.target`).
- `org.eclipse.jdt.feature.group` - Java Development Tools integration for hover, navigation, and searching (`target-platform.target`, `MANIFEST.MF`).
- `org.eclipse.ui.editors` - Framework for the text-based editor (`plugin.xml`, `MANIFEST.MF`).
- `org.eclipse.jface.text` - High-level text editor support for syntax highlighting and content assist (`MANIFEST.MF`).

**Infrastructure:**
- `org.eclipse.tycho:tycho-maven-plugin:5.0.2` - Drives the build process for Eclipse artifacts.
- `org.eclipse.core.resources` - Management of workspace resources and markers (`MANIFEST.MF`).

## Configuration

**Environment:**
- Maven Profiles - Configured for OS-specific SWT dependencies (Windows, Linux, macOS x86_64, macOS aarch64) in `bundles/io.github.xenogew.propedit/pom.xml`.
- Eclipse Target Platform - Defined in `target-platform/target-platform.target` pointing to Eclipse 2026-03 release.

**Build:**
- Root `pom.xml` - Parent configuration, dependency management, and module definitions.
- `bundles/io.github.xenogew.propedit/META-INF/MANIFEST.MF` - OSGi bundle metadata.
- `bundles/io.github.xenogew.propedit/plugin.xml` - Eclipse extension point registrations.

## Platform Requirements

**Development:**
- JDK 25
- Maven 3.9+ (via `mvnw`)

**Production:**
- Eclipse IDE (compatible with 2026-03 or newer)
- Java 25 Runtime (for standalone application)
- Support for Linux (GTK), Windows (win32), and macOS (cocoa) architectures (x86_64 and aarch64).

---

*Stack analysis: 2025-03-01*
