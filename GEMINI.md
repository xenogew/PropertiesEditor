# PropertiesEditor

A specialized Java `.properties` file editor that supports automatic Unicode escape conversion, available as both an Eclipse plugin and a standalone Swing application.

## Project Overview

- **Main Technologies:** Java (J2SE-1.4 to JavaSE-1.7), Swing, Eclipse Plugin Framework (SWT, JFace, OSGi).
- **Primary Functionality:** Simplifies the editing of Java properties files by allowing users to work with native characters (e.g., Japanese, Chinese) while automatically converting them to/from `\uXXXX` escapes during save/load operations.

## Key Technologies and Architecture

- **Eclipse Tycho**: Used for building Eclipse plugins, features, and products with Maven.
- **Eclipse 4 (e4)**: Demonstrates dependency injection (Jakarta Inject), the application model (`.e4xmi`), and CSS styling.
- **Generic Editor & TM4E**: Examples of extending the Eclipse Generic Editor with TextMate grammars (TM4E) for languages like Asciidoc.
- **Language Server Protocol (LSP)**: Integrations for Asciidoc via Language Servers.
- **OSGi**: The underlying modular system for all Eclipse plugins.
- **Java 25**: The project is configured to use JavaSE-25.

## Core Features

- **Unicode Escape Support:** Seamlessly handles non-ASCII characters by converting them to Unicode escapes.
- **Syntax Highlighting:** Provides visual distinction for keys, values, and comments in properties files.
- **Key Duplication Checking:** Identifies and marks duplicate keys to prevent configuration errors.
- **Folding:** Supports collapsing and expanding sections of the properties file.
- **Eclipse Integration:**
  - JDT hovers and completion proposals for properties.
  - Custom preference and property pages.
  - Content outline view.
- **Standalone Mode:** Can be run as a standard desktop application using Swing.

## Building and Running

### Prerequisites

- **Java 25**: Ensure you have a JDK 25 installed and your `JAVA_HOME` is set.
- **Maven**: The project includes a Maven wrapper (`mvnw`).

### Build Environment
This is a standard Eclipse project. It does not use modern build tools like Maven or Gradle.
- **Source Folder:** `src/`
- **Output Folder:** `bin/`
- **Classpath:** Managed by `.classpath` and `META-INF/MANIFEST.MF`.

### Running Standalone
The standalone application's entry point is:
`jp.gr.java_conf.ussiy.app.propedit.PropertiesEditor`

### Running as Plugin
The plugin activator is:
`jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.PropertiesEditorPlugin`

## Development Conventions

- **Language Support:** Localization is handled via `.properties` files (e.g., `lang.properties`, `plugin.properties`).
- **Icons:** Located in the `icons/` and `src/.../resource/` directories.
- **Packaging:** Configuration for the Eclipse plugin is defined in `plugin.xml` and `META-INF/MANIFEST.MF`.
