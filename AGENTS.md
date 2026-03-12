# Project Overview: PropertiesEditor

A specialized Java `.properties` file editor that supports automatic Unicode escape conversion (`\uXXXX` ↔ native characters), available as both an **Eclipse IDE plugin** and a **standalone Swing desktop application**.

- **Bundle Symbolic Name**: `jp.gr.java_conf.ussiy.app.propedit`
- **Bundle Version**: 6.0.5
- **Original Author**: Sou Miyazaki
- **Source**: Forked from [PropertiesEditor on OSDN](http://svn.osdn.net/svnroot/propedit/)
- **License**: See `LICENSE` file

---

## Current State (Legacy)

> **This section documents the codebase as it exists today, prior to any modernization.**

### Technologies

- **Java**: Targets `J2SE-1.4`, `J2SE-1.5`, `JavaSE-1.6`, `JavaSE-1.7` (as declared in `META-INF/MANIFEST.MF` `Bundle-RequiredExecutionEnvironment`)
- **Eclipse Platform**: Eclipse 3.x API style — uses `AbstractUIPlugin`, `TextEditor`, `IAction`-based contributions, `FieldEditorPreferencePage`, classic extension points
- **OSGi**: Single bundle, singleton, lazy activation
- **Swing**: Standalone desktop app using `javax.swing` (AWT/Swing)
- **Build System**: Eclipse PDE native — `build.properties` + `META-INF/MANIFEST.MF`. **No Maven, no Tycho, no Gradle.**
- **Localization**: English (`plugin.properties`, `lang.properties`) and Japanese (`plugin_ja.properties`, `lang_ja.properties`)

### Known Legacy Patterns

- **Raw types everywhere** — `ArrayList`, `HashMap`, `List`, `Iterator` without generics
- **`StringBuffer`** used instead of `StringBuilder`
- **`new Integer()` / `new Boolean()`** — deprecated boxed constructors
- **No `@Override` annotations** on overridden methods
- **`getAdapter(Class)`** — untyped adapter pattern (pre-generics)
- **Manual for-loops** with index counters instead of enhanced for-each or streams
- **`e.printStackTrace()`** used for error handling in several places
- **No unit tests** present in the codebase

---

## Project Structure

```
PropertiesEditor/
├── META-INF/MANIFEST.MF          # OSGi bundle manifest
├── plugin.xml                     # Eclipse extension point declarations
├── plugin.properties              # Plugin localization (English)
├── plugin_ja.properties           # Plugin localization (Japanese)
├── build.properties               # PDE build configuration
├── about.ini                      # About dialog info
├── icons/                         # Plugin icons (GIF format)
├── schema/                        # Extension point schemas (.exsd)
│   ├── listeners.exsd
│   └── hyperlinkdetectors.exsd
├── license/                       # License files
└── src/
    └── jp/gr/java_conf/ussiy/
        ├── app/propedit/                          # Standalone Swing app
        │   ├── PropertiesEditor.java              # Main class (entry point)
        │   ├── PropertiesEditorFrame.java         # Main Swing JFrame (52KB)
        │   ├── PropertiesEditorFrame_AboutBox.java
        │   ├── BaseDialog.java
        │   ├── SearchTextDialog.java
        │   ├── ReplaceTextDialog.java
        │   ├── UnicodeDialog.java
        │   ├── EncodeSelectPanel.java
        │   ├── JSelectCodeFileChooser.java
        │   ├── lang.properties                    # App i18n (English)
        │   ├── lang_ja.properties                 # App i18n (Japanese)
        │   ├── bean/
        │   │   ├── AppSetting.java                # Application settings bean
        │   │   └── Encode.java                    # Encoding data bean
        │   ├── util/
        │   │   ├── EncodeChanger.java             # Core: Unicode ↔ \uXXXX conversion
        │   │   ├── EncodeManager.java             # Encoding utilities
        │   │   ├── FileOpener.java                # File I/O helper
        │   │   ├── AlreadyFileLockException.java  # Custom exception
        │   │   ├── Messages.java                  # NLS message helper
        │   │   └── messages.properties            # Message strings
        │   └── eclipse/plugin/                    # Eclipse plugin code
        │       ├── PropertiesEditorPlugin.java    # Bundle activator (AbstractUIPlugin)
        │       ├── action/                        # Editor actions (IAction-based)
        │       │   ├── ToggleCommentAction.java
        │       │   ├── CollapseAllFoldingAction.java
        │       │   ├── ExpandAllFoldingAction.java
        │       │   └── ShowUnicodeEscAction.java
        │       ├── analyze/                       # Property file parsing
        │       │   ├── PropertyAnalyzer.java
        │       │   └── PropertyElement.java
        │       ├── checker/                       # Duplicate key detection
        │       │   ├── CheckAndMarkDuplicateKey.java
        │       │   └── CheckAndMarkResourceVisitor.java
        │       ├── editors/                       # Core editor components
        │       │   ├── PropertiesEditor.java      # Main editor (extends TextEditor)
        │       │   ├── PropertiesConfiguration.java  # SourceViewerConfiguration
        │       │   ├── PropertiesDocumentProvider.java  # Document load/save with Unicode conversion
        │       │   ├── PropertiesPartitionScanner.java  # Partition scanner (comment/key/separator/value)
        │       │   ├── PropertiesScanner.java     # Token scanner for syntax highlighting
        │       │   ├── PropertiesReconcilingStrategy.java  # Reconciling strategy (folding)
        │       │   ├── PropertiesDoubleClickStrategy.java
        │       │   ├── ColorManager.java          # SWT color management
        │       │   ├── ComboFieldEditor.java      # Custom field editor
        │       │   ├── NonRuleBasedDamagerRepairer.java
        │       │   ├── detector/
        │       │   │   └── IHyperlinkDetector.java  # Custom hyperlink detector interface
        │       │   ├── rules/                     # Partition scanning rules
        │       │   │   ├── CommentLineRule.java
        │       │   │   ├── SeparatorRule.java
        │       │   │   ├── ValueRule.java
        │       │   │   └── SkipHeadSpaceRule.java
        │       │   └── view/outline/
        │       │       └── PropertiesContentOutlinePage.java
        │       ├── jdt/                           # JDT integration
        │       │   ├── hover/
        │       │   │   └── PropertiesHover.java   # Java editor hover (shows property values)
        │       │   └── proposal/
        │       │       └── PropertiesCompletionProposalComputer.java
        │       ├── listener/
        │       │   ├── IPropertiesDocumentListener.java  # Extension point interface
        │       │   └── DuplicationKeyCheckListener.java
        │       ├── preference/                    # Preference pages
        │       │   ├── PreferenceInitializer.java
        │       │   ├── PropertiesPreference.java
        │       │   ├── PropertiesEditorPreference.java
        │       │   └── PropertiesEditorDuplicationCheckerPreference.java
        │       ├── property/                      # Per-project property page
        │       │   ├── PropertiesProperty.java
        │       │   └── PropertyUtil.java
        │       ├── resources/
        │       │   └── Messages.java              # Eclipse NLS message helper
        │       ├── startup/
        │       │   └── Startup.java               # IStartup implementation
        │       ├── util/                          # Eclipse-specific utilities
        │       └── wizard/
        │           └── BasicNewPropertiesFileWizard.java
        ├── io/                                    # Custom I/O classes
        ├── swing/                                 # Swing utility classes
        │   ├── filechooser/
        │   ├── plaf/basic/
        └── util/
            └── StringUtil.java                    # String utility
```

---

## Key Features

- **Unicode Escape Conversion**: Automatically converts `\uXXXX` → native characters on load, and native → `\uXXXX` on save. Supports upper/lowercase hex. Option to skip comments.
- **Syntax Highlighting**: Partition-based coloring for comments (`#`, `!`), keys, separators (`=`, `:`), and values. Colors are configurable via preferences.
- **Duplicate Key Detection**: Scans properties files for duplicate keys and marks them with Eclipse problem markers (`IMarker.SEVERITY_WARNING`).
- **Code Folding**: Optional projection-based folding support with "collapse all" / "expand all" actions.
- **Content Outline**: `IContentOutlinePage` showing property keys in the Outline view.
- **JDT Integration**:
  - **Hover**: Shows property values when hovering over property key strings in Java source code.
  - **Completion Proposals**: Suggests property keys inside Java string literals.
- **Hyperlink Detection**: Custom `IHyperlinkDetector` extension point for navigating from property references.
- **Preference Pages**: Color settings, folding options, Unicode conversion options, duplicate key checking toggle.
- **Per-Project Properties**: Project-level override of global preferences via property pages.
- **New File Wizard**: "New Properties File" wizard under the Java category.
- **Keyboard Shortcuts**: `Ctrl+/` (toggle comment), `Ctrl+I` (show unicode escape)
- **Standalone Swing App**: Full desktop application with file open/save, search/replace, encoding selection, and Unicode conversion.
- **Localization**: English and Japanese (via `plugin.properties` / `plugin_ja.properties` and `lang.properties` / `lang_ja.properties`).

---

## Extension Points (Defined by This Plugin)

| Extension Point ID                                      | Schema                           | Purpose                                                                                          |
| ------------------------------------------------------- | -------------------------------- | ------------------------------------------------------------------------------------------------ |
| `jp.gr.java_conf.ussiy.app.propedit.listeners`          | `schema/listeners.exsd`          | Register `IPropertiesDocumentListener` implementations to hook into document load/save lifecycle |
| `jp.gr.java_conf.ussiy.app.propedit.hyperlinkdetectors` | `schema/hyperlinkdetectors.exsd` | Register custom hyperlink detectors for the properties editor                                    |

---

## Eclipse Extensions Used (in `plugin.xml`)

- `org.eclipse.ui.editors` — registers the PropertiesEditor
- `org.eclipse.ui.preferencePages` — 3 preference pages
- `org.eclipse.core.runtime.preferences` — preference initializer
- `org.eclipse.ui.propertyPages` — per-project properties
- `org.eclipse.ui.popupMenus` — context menu actions (legacy `viewerContribution`)
- `org.eclipse.ui.contexts` — editor scope for keybindings
- `org.eclipse.ui.commands` — 4 commands (toggle comment, collapse/expand, show unicode)
- `org.eclipse.ui.editorActions` — toolbar/menu contributions
- `org.eclipse.ui.bindings` — `Ctrl+/` and `Ctrl+I`
- `org.eclipse.core.resources.markers` — custom duplication marker
- `org.eclipse.jdt.ui.javaCompletionProposalComputer` — property key proposals in Java editor
- `org.eclipse.core.contenttype.contentTypes` — `.properties` content type
- `org.eclipse.ui.newWizards` — new properties file wizard
- `org.eclipse.jdt.ui.javaEditorTextHovers` — hover showing property values
- `org.eclipse.ui.startup` — early startup hook

---

## Building and Running (Current — PDE Native)

### Prerequisites

- **Eclipse IDE**: Eclipse IDE for RCP and RAP Developers (or Eclipse for Committers)
- **Java**: JDK 7+ (to match current `Bundle-RequiredExecutionEnvironment`)

### Import and Run

1. **Import into Eclipse**: File → Import → Existing Projects into Workspace → select the `PropertiesEditor` root directory.
2. **Target Platform**: The plugin resolves against the running Eclipse's target platform. No custom `.target` file exists.
3. **Run as Eclipse Application**: Right-click the project → Run As → Eclipse Application. The PropertiesEditor will be available in the launched Eclipse instance for `.properties` files.

### Standalone Swing App

The standalone application entry point is `jp.gr.java_conf.ussiy.app.propedit.PropertiesEditor` (declared as `Main-Class` in `MANIFEST.MF`). Run it as a standard Java application.

---

## Development Conventions (Current)

- **OSGi Metadata**: All bundle dependencies are in `META-INF/MANIFEST.MF` (`Require-Bundle`).
- **Extensions**: All UI contributions are declared in `plugin.xml`.
- **Localization**: Externalized strings use `%key` references in `plugin.xml` resolved by `plugin.properties` / `plugin_ja.properties`. Code uses `ResourceBundle` and `Messages.getString()`.
- **Coding Style**: Java 1.4 era — no generics, no annotations, no lambdas. Uses `$NON-NLS-1$` comments for string externalization.
- **Encoding**: UTF-8.

---

## Modernization Roadmap

> **This section provides step-by-step instructions for an AI agent or developer to modernize the project from its legacy Java 1.4/Eclipse 3.x state to modern Java 21+ and latest Eclipse.**

### Phase 1: Build System — Migrate to Maven + Eclipse Tycho ✅ DONE

**Goal**: Replace PDE native build with Maven/Tycho so the plugin can be built from the command line.

**Completed**. The plugin now builds with `./mvnw clean verify` using:

- **Tycho 5.0.2** (bundles ECJ 3.44.0, supports up to Java 25)
- **Eclipse 2024-12** target platform (`target-platform/target-platform.target`)
- **JavaSE-25** BREE (upgraded from J2SE-1.4–1.7 as part of this phase)
- **Bundle-Version**: `6.0.5.qualifier` (Tycho requires `.qualifier` suffix for SNAPSHOT builds)
- **`.mvn/jvm.config`**: Sets `jdk.xml.maxGeneralEntitySizeLimit=0` to handle large Eclipse p2 metadata on Java 17+

Files created/modified:

- `pom.xml` — `eclipse-plugin` packaging with Tycho 5.0.2
- `target-platform/target-platform.target` — Eclipse 2024-12 p2 target
- `META-INF/MANIFEST.MF` — BREE → `JavaSE-25`, version → `6.0.5.qualifier`, removed `Eclipse-LazyStart`
- `build.properties` — added `javacSource=25`, `javacTarget=25`
- `.gitignore` — added `target/`, `bin/`, `*.target.resolved`
- `.mvn/jvm.config` — JVM XML parser limits for p2 metadata
- `mvnw`, `mvnw.cmd`, `.mvn/wrapper/` — Maven wrapper

### Phase 2: Java Version Upgrade ✅ DONE (merged into Phase 1)

**Goal**: Upgrade from J2SE-1.4 → JavaSE-25.

Completed as part of Phase 1. BREE is now `JavaSE-25`, `javacSource=25`, `javacTarget=25`. The build compiles all 69 source files successfully with `--release 25`.

### Phase 3: Java Language Modernization

**Goal**: Modernize Java code to use modern language features. Apply these changes file by file.

#### 3a. Add Generics (Remove Raw Types)

Every raw `ArrayList`, `HashMap`, `List`, `Iterator`, `Map` must be parameterized:

| File                              | Change                                                                                               |
| --------------------------------- | ---------------------------------------------------------------------------------------------------- |
| `PropertiesEditor.java` (editors) | `ArrayList` → `ArrayList<Position>`, `Iterator` → `Iterator<ProjectionAnnotation>`                   |
| `PropertiesConfiguration.java`    | `List` → `List<IHyperlinkDetector>`, `ArrayList` → `ArrayList<IHyperlinkDetector>`                   |
| `PropertiesDocumentProvider.java` | `List` → `List<IPropertiesDocumentListener>`, `ArrayList` → `ArrayList<IPropertiesDocumentListener>` |
| `PropertiesPartitionScanner.java` | `List` → `List<IPredicateRule>`                                                                      |
| `CheckAndMarkDuplicateKey.java`   | `Map` → `Map<String, Integer>`, `List` → `List<String>`                                              |
| All for-loops using index         | Convert to enhanced for-each where applicable                                                        |

#### 3b. Replace Deprecated Constructors

| Pattern              | Replacement                            |
| -------------------- | -------------------------------------- |
| `new Integer(value)` | `Integer.valueOf(value)` or autoboxing |
| `new Boolean(value)` | `Boolean.valueOf(value)` or autoboxing |

Files affected: `CheckAndMarkDuplicateKey.java`, `PropertiesEditor.java` (editors).

#### 3c. `StringBuffer` → `StringBuilder`

Replace all `StringBuffer` with `StringBuilder` (none of the usages are shared across threads):

Files affected: `EncodeChanger.java`, `CheckAndMarkDuplicateKey.java`.

#### 3d. Add `@Override` Annotations

Add `@Override` to every method that overrides a superclass method or implements an interface method. This affects virtually every file in the `eclipse/plugin/` package tree.

#### 3e. Use Try-with-Resources

Replace manual `try/finally` resource closing with try-with-resources:

Files affected: `CheckAndMarkDuplicateKey.java` (`BufferedReader`), `EncodeChanger.java`, `FileOpener.java`.

#### 3f. Use Diamond Operator

Replace `new ArrayList<Type>()` with `new ArrayList<>()` (after generics are added).

#### 3g. Use `var` (Local Variable Type Inference)

Use `var` for local variables where the type is obvious from the right-hand side.

#### 3h. Use Lambdas and Method References

Replace anonymous inner classes with lambdas where a functional interface is used (e.g., `Runnable`, `SelectionListener`, `ModifyListener`). Primarily applies to the Swing standalone app (`PropertiesEditorFrame.java`).

#### 3i. Use `String.replace()` Instead of Manual StringBuffer Replacement

In `CheckAndMarkDuplicateKey.replace()` — replace the manual `StringBuffer`-based string replacement with `String.replace()`.

#### 3j. Records for Simple Data Holders (Java 16+)

Evaluate `AppSetting.java` and `Encode.java` in `bean/` — if they are simple data carriers, convert to `record` types. Only do this if they have no mutable state that needs to be preserved.

### Phase 4: Eclipse API Modernization

**Goal**: Replace deprecated Eclipse 3.x APIs with modern equivalents.

#### 4a. Deprecated `org.eclipse.ui.popupMenus` → Commands/Handlers

The `plugin.xml` uses the legacy `org.eclipse.ui.popupMenus` extension point (`viewerContribution`). Migrate to:

- `org.eclipse.ui.menus` (with `menuContribution` elements)
- `org.eclipse.ui.handlers` (with `IHandler` / `AbstractHandler` implementations)

The existing `IAction`-based classes in `action/` should be refactored to extend `AbstractHandler` and implement `execute(ExecutionEvent)`.

#### 4b. Deprecated `org.eclipse.ui.editorActions` → `org.eclipse.ui.menus`

Same migration as 4a — move toolbar/menu contributions from `editorActions` to `menus` with `menuContribution` targeting `toolbar:` and `menu:` URIs.

#### 4c. `getAdapter(Class)` → Typed `getAdapter(Class<T>)`

In `PropertiesEditor.java` (editors):

```java
// Before
public Object getAdapter(Class adapter)
// After
@SuppressWarnings("unchecked")
public <T> T getAdapter(Class<T> adapter)
```

#### 4d. `Eclipse-LazyStart` → Remove

Remove `Eclipse-LazyStart: true` from `MANIFEST.MF` — it is superseded by `Bundle-ActivationPolicy: lazy` (already present).

#### 4e. Review `ILog` Usage

The pattern `PropertiesEditorPlugin.getDefault().getLog()` is still valid in modern Eclipse. However, consider adding a utility method or using `Platform.getLog(Class)` (available since Eclipse 4.x).

#### 4f. Replace `e.printStackTrace()` with Proper Logging

Several classes use `e.printStackTrace()`. Replace with:

```java
ILog log = PropertiesEditorPlugin.getDefault().getLog();
log.log(new Status(IStatus.ERROR, PropertiesEditorPlugin.PLUGIN_ID, e.getMessage(), e));
```

### Phase 5: Swing Standalone App Modernization

**Goal**: Modernize the Swing desktop app (lower priority than Eclipse plugin).

1. `PropertiesEditorFrame.java` (52KB) — refactor into smaller classes, use lambdas for event listeners.
2. Replace `UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())` with FlatLaf or similar modern look-and-feel.
3. Use `var`, enhanced for-each, and streams where appropriate.
4. Evaluate whether the standalone app should be preserved long-term or deprecated in favor of Eclipse-only usage.

### Phase 6: Testing

**Goal**: Add unit tests (currently none exist).

1. Create a test fragment or test source folder.
2. Add JUnit 5 dependency.
3. Write tests for core logic:
   - `EncodeChanger` — Unicode ↔ escape conversion (most critical).
   - `PropertyAnalyzer` — property file parsing.
   - `CheckAndMarkDuplicateKey` — duplicate key detection logic.
4. If using Tycho, configure `tycho-surefire-plugin` to run tests during build.

### Phase 7: Cleanup and Polish

1. **Icons**: Replace `.gif` icons with `.png` or `.svg` for HiDPI support.
2. **Bundle version**: Bump to `7.0.0` to indicate the major modernization.
3. **README.md**: Update with proper project description, build instructions, and screenshots.
4. **GEMINI.md**: Update or remove — it contains the same inaccuracies that were in the old `AGENTS.md`.
5. **`.gitignore`**: Ensure `bin/`, `target/`, `.classpath`, `.project`, `.settings/` are ignored.

---

## Critical Files for Any Change

When making modifications, these files are the most important to understand:

| File                              | Why                                                                    |
| --------------------------------- | ---------------------------------------------------------------------- |
| `META-INF/MANIFEST.MF`            | Bundle identity, dependencies, Java version, activator                 |
| `plugin.xml`                      | All Eclipse extension point declarations                               |
| `build.properties`                | PDE build configuration (source/output folders, included files)        |
| `PropertiesDocumentProvider.java` | Core load/save logic with Unicode conversion — the heart of the plugin |
| `PropertiesEditor.java` (editors) | Main editor class — folding, outline, adapter                          |
| `PropertiesConfiguration.java`    | Source viewer config — syntax highlighting, reconciling, hyperlinks    |
| `EncodeChanger.java`              | Core Unicode ↔ escape conversion algorithm                             |
| `CheckAndMarkDuplicateKey.java`   | Duplicate key detection and marker creation                            |
| `PropertiesEditorPlugin.java`     | Plugin activator — singleton, resource bundle                          |

---

## Dependencies (from `MANIFEST.MF` `Require-Bundle`)

| Bundle                                 | Resolution   |
| -------------------------------------- | ------------ |
| `org.eclipse.core.resources`           | Required     |
| `org.eclipse.jface.text`               | Required     |
| `org.eclipse.ui`                       | Required     |
| `org.eclipse.ui.editors`               | Required     |
| `org.eclipse.ui.ide`                   | Required     |
| `org.eclipse.core.runtime` (≥3.2.0)    | Required     |
| `org.eclipse.ui.views`                 | Required     |
| `org.eclipse.ui.workbench.texteditor`  | Required     |
| `org.eclipse.jdt.core`                 | **Optional** |
| `org.eclipse.core.filesystem` (≥1.0.0) | Required     |
| `org.eclipse.jdt.ui` (≥3.2.0)          | Required     |
