# Architecture

**Analysis Date:** 2025-03-14

## Pattern Overview

**Overall:** Multi-page Form Editor (Eclipse Plug-in Architecture)

**Core Patterns:**
- **MVVM (Model-View-ViewModel)**: Applied to the multi-mode grid editor.
    - **Model**: `PropertyBundleModel` (Raw data and workspace persistence).
    - **ViewModel**: `PropertiesGridViewModel` (Logic for spanning, debouncing, and state synchronization).
    - **View**: `PropertiesGridPage` (Nebula NatTable UI and event handling).
- **MultiPageEditorPart**: Coordinates multiple views (Grid vs. Source).
- **Flyweight Editor**: Used in the virtualized grid to minimize widget allocation.

## Layers

**UI Layer:**
- Purpose: Provides the editing interface within the Eclipse IDE.
- Location: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/`
- Contains: `PropEditorX` (coordinator), `PropertiesGridPage` (grid view), `PropertiesSourceEditor` (text view).
- Depends on: Model Layer, Eclipse UI/Forms API.
- Used by: Eclipse Workbench.

**Model Layer:**
- Purpose: Manages the state of properties files and their relationships.
- Location: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertyBundleModel.java`
- Contains: `PropertyBundleModel`, `ProjectProperties` (workspace-wide cache).
- Depends on: Eclipse Resources API.
- Used by: UI Layer, Integration Layer.

**Integration Layer (JDT):**
- Purpose: Connects the properties data to the Java editing experience.
- Location: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/jdt/`
- Contains: Content assist, hover information, and hyperlink detection.
- Depends on: Model Layer, Eclipse JDT API.
- Used by: Eclipse Java Editor.

## Data Flow

**Editor Initialization:**

1. User opens a `.properties` file.
2. `PropEditorX` is instantiated.
3. `PropertyBundleModel` is created, which "discovers" sibling locale files (e.g., `messages_ja.properties` for `messages.properties`).
4. UI pages are populated from the model.

**Editing & Saving:**

1. Changes in `PropertiesGridPage` update the `PropertyBundleModel` and mark the editor dirty.
2. Changes in `PropertiesSourceEditor` use standard Eclipse text editor mechanisms.
3. On Save (`doSave`), `PropEditorX` saves all source editors (flushing to disk) and then saves the `PropertyBundleModel` state (handling grid-based changes).

**Workspace-wide Property Cache:**

1. `Startup` class initializes `ProjectProperties` cache on IDE start.
2. `ResourceChangeListener` monitors file changes and updates the cache.
3. JDT features (assist/hover) query this cache to provide real-time suggestions.

## Key Abstractions

**PropertyBundleModel:**
- Purpose: Aggregates multiple `.properties` files representing different locales of the same bundle.
- Examples: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertyBundleModel.java`
- Pattern: Model-View-Controller (Model).

**ProjectProperties:**
- Purpose: Singleton cache providing workspace-wide access to all property keys and values for JDT integration.
- Examples: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/util/ProjectProperties.java`
- Pattern: Singleton / Repository.

## Entry Points

**PropEditorX:**
- Location: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropEditorX.java`
- Triggers: Opening a `.properties` file.
- Responsibilities: Coordinating multi-page views and managing the save lifecycle.

**Startup:**
- Location: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/startup/Startup.java`
- Triggers: Eclipse Workbench startup.
- Responsibilities: Initializing workspace-wide property cache and starting resource listeners.

## Error Handling

**Strategy:** Centralized logging via Plugin Activator.

**Patterns:**
- Errors are logged to the Eclipse Error Log using `PropEditorXPlugin.getDefault().error(message, exception)`.
- Graceful degradation: If a locale file cannot be loaded, it's skipped or displayed as empty rather than crashing the editor.

## Cross-Cutting Concerns

**Logging:** Handled by `PropEditorXPlugin` using `org.eclipse.core.runtime.ILog`.
**Resource Monitoring:** Implemented in `Startup` via `IResourceChangeListener` to maintain cache consistency.
**Internationalization:** Uses `plugin.properties` for plugin-level I18N and `Messages.java` for internal strings.

---

*Architecture analysis: 2025-03-14*
