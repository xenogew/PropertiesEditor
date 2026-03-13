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

### Phase 3: Java Language Modernization ✅ DONE

**Goal**: Modernize Java code to use modern language features. Apply these changes file by file.

#### 3a. Add Generics (Remove Raw Types) ✅ DONE

Every raw `ArrayList`, `HashMap`, `List`, `Iterator`, `Map`, `Vector` must be parameterized:

| File                              | Change                                                                                               |
| --------------------------------- | ---------------------------------------------------------------------------------------------------- |
| `PropertiesEditor.java` (editors) | `ArrayList` → `ArrayList<Position>`, `Iterator` → `Iterator<ProjectionAnnotation>`                   |
| `PropertiesConfiguration.java`    | `List` → `List<IHyperlinkDetector>`, `ArrayList` → `ArrayList<IHyperlinkDetector>`                   |
| `PropertiesDocumentProvider.java` | `List` → `List<IPropertiesDocumentListener>`, `ArrayList` → `ArrayList<IPropertiesDocumentListener>` |
| `PropertiesPartitionScanner.java` | `List` → `List<IPredicateRule>`                                                                      |
| `CheckAndMarkDuplicateKey.java`   | `Map` → `Map<String, Integer>`, `List` → `List<String>`                                              |
| `EncodeManager.java`              | `Vector` → `List<Encode>`, raw `List` → `List<String>`, return type `Vector` → `List<Encode>`        |
| `EncodeSelectPanel.java`          | `Vector` → `List<Encode>`, removed unchecked casts, enhanced for-each loops                          |
| `JExtendedPopupComboBox.java`     | Raw `Vector` → `Vector<?>`, `@SuppressWarnings("rawtypes")` on class (extends raw `JComboBox`)       |
| `JFontChooserDialog.java`         | Raw `JComboBox` → `JComboBox<String>`                                                                |
| `JExtendedPopupComboBoxUI.java`   | Raw `ComboBoxModel` → `ComboBoxModel<?>`, raw `JComboBox` → `JComboBox<?>`                           |
| `PropertiesEditorFrame.java`      | Raw `java.util.List` → `java.util.List<File>` in `DropHandler.drop()`                                |
| All for-loops using index         | Convert to enhanced for-each where applicable                                                        |

#### 3b. Replace Deprecated Constructors ✅ DONE

| Pattern              | Replacement                            |
| -------------------- | -------------------------------------- |
| `new Integer(value)` | `Integer.valueOf(value)` or autoboxing |
| `new Boolean(value)` | `Boolean.valueOf(value)` or autoboxing |

Files affected: `CheckAndMarkDuplicateKey.java`, `PropertiesEditor.java` (editors).

#### 3c. `StringBuffer` → `StringBuilder` ✅ DONE

Replace all `StringBuffer` with `StringBuilder` (none of the usages are shared across threads):

Files affected: `EncodeChanger.java`, `CheckAndMarkDuplicateKey.java`, `PropertiesEditorFrame.java` (`ajustLineNumber`).

#### 3d. Add `@Override` Annotations ✅ DONE

Add `@Override` to every method that overrides a superclass method or implements an interface method. This affects virtually every file in the `eclipse/plugin/` package tree.

#### 3e. Use Try-with-Resources ✅ DONE

Replace manual `try/finally` resource closing with try-with-resources:

Files affected: `CheckAndMarkDuplicateKey.java` (`BufferedReader`), `EncodeChanger.java`, `FileOpener.java`.

#### 3f. Use Diamond Operator ✅ DONE

Replace `new ArrayList<Type>()` with `new ArrayList<>()` (after generics are added).

#### 3g. Use `var` (Local Variable Type Inference) ✅ DONE

Use `var` for local variables where the type is obvious from the right-hand side.

Files affected: `PropertiesEditorFrame.java` (`new_actionPerformed`, `version_actionPerformed`, `selectOpenFile`, `ajustLineNumber`), `JFontChooserDialog.java` (`initialize`), `EncodeSelectPanel.java` (`getSelectedEncode`).

#### 3h. Use Lambdas and Method References ✅ DONE

Replace anonymous inner classes with lambdas where a functional interface is used (e.g., `Runnable`, `ActionListener`). Non-functional anonymous classes (e.g., `KeyAdapter`, `DropTargetListener`) retained with `@Override` added.

Files affected: `PropertiesEditorFrame.java` (30+ lambda conversions), `JFontChooserDialog.java` (7 lambdas), `ReplaceTextDialog.java` (2), `SearchTextDialog.java` (2), `UnicodeDialog.java` (1), `PropertiesReconcilingStrategy.java` (1).

#### 3i. Use `String.replace()` Instead of Manual StringBuffer Replacement ✅ DONE

In `CheckAndMarkDuplicateKey.replace()` — replaced the manual `StringBuilder`-based string replacement with `String.replace()`.

#### 3j. Records for Simple Data Holders (Java 16+) ✅ DONE

- **`Encode.java`**: Converted to `record Encode(int no, String name, String code)`. Updated all callers (`EncodeSelectPanel.java`) from bean-style getters (`getName()`) to record accessors (`name()`).
- **`AppSetting.java`**: Remains a class — relies on mutable singleton state, Java serialization (`readObject`/`writeObject`), and setter-based mutation. Javadoc note added documenting future record migration potential after architectural changes (e.g., replacing `ObjectOutputStream` persistence with JSON/TOML config).

### Phase 4: Eclipse API Modernization ✅ DONE

**Goal**: Replace deprecated Eclipse 3.x APIs with modern equivalents.

#### 4a. Deprecated `org.eclipse.ui.popupMenus` → Commands/Handlers ✅ DONE

Migrated all 4 action classes from `IEditorActionDelegate` to `AbstractHandler`:

| Class                      | Change                                                                                                     |
| -------------------------- | ---------------------------------------------------------------------------------------------------------- |
| `ToggleCommentAction`      | `implements IEditorActionDelegate` → `extends AbstractHandler`, `run(IAction)` → `execute(ExecutionEvent)` |
| `CollapseAllFoldingAction` | same                                                                                                       |
| `ExpandAllFoldingAction`   | same                                                                                                       |
| `ShowUnicodeEscAction`     | same                                                                                                       |

All classes now use `HandlerUtil.getActiveEditor(event)` with pattern matching (`instanceof PropertiesEditor editor`) instead of storing a field via `setActiveEditor()`.

`plugin.xml`: Removed `org.eclipse.ui.popupMenus` extension. Added `org.eclipse.ui.handlers` with `<handler>` elements per command, scoped via `<activeWhen>` to the editor ID.

#### 4b. Deprecated `org.eclipse.ui.editorActions` → `org.eclipse.ui.menus` ✅ DONE

`plugin.xml`: Removed `org.eclipse.ui.editorActions` extension. Added `org.eclipse.ui.menus` with two `<menuContribution>` blocks:

- `popup:#PropertiesEditorContext?after=additions` — context menu (replaces `popupMenus`)
- `menu:edit?after=additions` — editor menu bar (replaces `editorActions`), with `<visibleWhen>` scoped to editor ID

#### 4c. `getAdapter(Class)` → Typed `getAdapter(Class<T>)` ✅ DONE

In `PropertiesEditor.java` (editors): updated to `public <T> T getAdapter(Class<T> adapter)` with `@SuppressWarnings("unchecked")` and `(T)` cast.

#### 4d. `Eclipse-LazyStart` → Remove ✅ DONE (already absent)

`Eclipse-LazyStart: true` was not present in `MANIFEST.MF` — only `Bundle-ActivationPolicy: lazy` exists. No change needed.

#### 4e. Review `ILog` Usage ✅ DONE

Added `PropertiesEditorPlugin.log()` static helper using `Platform.getLog(PropertiesEditorPlugin.class)` — available even before the activator starts. Existing callers remain valid.

#### 4f. Replace `e.printStackTrace()` with Proper Logging ✅ DONE

Replaced all 14 `e.printStackTrace()` / `ex.printStackTrace()` calls across 11 files with `java.util.logging.Logger`:

| File                                  | Count |
| ------------------------------------- | ----- |
| `PropertiesEditorFrame.java`          | 5     |
| `LockableFileOutputStream.java`       | 3     |
| `PropertiesEditor.java` (Swing main)  | 1     |
| `JSelectCodeFileChooser.java`         | 1     |
| `PropertiesEditorFrame_AboutBox.java` | 1     |
| `JFontChooserDialog.java`             | 1     |
| `UnicodeDialog.java`                  | 1     |
| `SearchTextDialog.java`               | 1     |
| `ReplaceTextDialog.java`              | 1     |
| `EncodeSelectPanel.java`              | 1     |

Pattern: `private static final Logger LOG = Logger.getLogger(ClassName.class.getName());` + `LOG.log(Level.SEVERE, e.getMessage(), e);`

### Phase 5: Swing Standalone App Modernization — DONE

**Goal**: Modernize the Swing desktop app (lower priority than Eclipse plugin).

1. **`PropertiesEditorFrame.java` refactoring** — DONE
   Extracted 6 inner classes to top-level package-private files (same package):
   - `UndoHandler.java` — `UndoableEditListener` implementation
   - `UndoAction.java` — Undo `AbstractAction`
   - `RedoAction.java` — Redo `AbstractAction`
   - `DropHandler.java` — `DropTargetListener` for drag-and-drop
   - `PrintMonitor.java` — Print progress monitor (`Printable`)
   - `EditorPrinter.java` — Editor content printer (`Printable`)
     Each extracted class takes a `PropertiesEditorFrame` reference. Fields `undoAction`, `redoAction`, `editTextArea`, `pageFormat` and methods `openFile()`, `checkSave()` widened from `private` to package-private.
     File reduced from 1,728 → 1,454 lines.

2. **FlatLaf look-and-feel** — DONE
   - Added `lib/flatlaf-3.6.jar` (~900KB)
   - `MANIFEST.MF`: Added `Bundle-ClassPath: ., lib/flatlaf-3.6.jar`
   - `build.properties`: Added `lib/` to `bin.includes`
   - `PropertiesEditor.main()`: Uses `FlatLightLaf.setup()` with system L&F fallback; also registers `FlatDarkLaf` as switchable option
   - L&F radio menu in `PropertiesEditorFrame` automatically picks up FlatLaf entries from `UIManager.getInstalledLookAndFeels()`

3. **`var` / enhanced for-each / streams sweep** — DONE
   - Converted 7 index-based for-loops in `PropertiesEditorFrame.java` to enhanced for-each (L&F menu population, L&F matching in `initialize()`, `actionPerformed()`)
   - Converted 1 index-based for-loop in `PropertiesEditor.java` (editors) `updateFoldingStructure()`
   - Applied `var` to ~15 local variable declarations across `PropertiesEditorFrame.java` and `PropertiesEditor.java` (editors)
   - Remaining ~17 index-based for-loops in `SeparatorRule`, `ValueRule`, `ValueRuleForWhiteSpace`, `EncodeChanger`, `CheckAndMarkDuplicateKey`, `PropertiesReconcilingStrategy`, `PropertiesOutlineContentProvider` — all genuinely require the index for character scanning or `scanner.unread()` operations; not candidates for conversion

4. **Standalone app evaluation** — DONE
   **Decision: Preserve the standalone Swing app.** Rationale:
   - The standalone app shares core code (`EncodeChanger`, `EncodeManager`, `FileOpener`) with the Eclipse plugin — no maintenance duplication
   - It provides value for users who need quick `.properties` file editing without Eclipse
   - FlatLaf gives it a modern appearance competitive with native apps
   - Future option: repackage with `jpackage` for native distribution (installer, no JRE required)
   - Lower priority for further investment — focus future phases on Eclipse plugin and testing

### Phase 6: Testing — DONE

**Goal**: Add unit tests (currently none exist).

1. **Test infrastructure** — DONE
   - Created `src/test/java` directory
   - Added JUnit 5 (`junit-jupiter` 5.11.4) dependency to `pom.xml`
   - Added `maven-surefire-plugin` 3.5.2 + `maven-compiler-plugin` 3.14.0 to compile and run plain Java tests alongside Tycho
   - Configured `tycho-compiler-plugin` to exclude `**/test/**` so Tycho doesn't compile test sources
   - Tests run during `mvn verify` via surefire (no OSGi runtime needed for pure-Java tests)

2. **`EncodeChangerTest`** — DONE (23 tests)
   - `unicode2UnicodeEsc`: ASCII-only, Japanese, mixed, empty, null, uppercase/lowercase, hex zero-padding
   - `unicodeEsc2Unicode`: valid lowercase/uppercase `\u`, invalid hex, partial sequences, empty, null, mixed, non-u escapes
   - Round-trip: Japanese, ASCII, mixed
   - `unicode2UnicodeEscWithoutComment`: comment preservation (`#`, `!`), continuation lines, no trailing newline

3. **`StringUtilTest`** — DONE (10 tests)
   - `removeCarriageReturn`: Windows `\r\n`, standalone `\r`, no `\r`, empty
   - `escapeHtml`: `&`, `<`, `>`, `"`, null, combined

4. **`CheckAndMarkDuplicateKey` refactoring + tests** — DONE (18 tests)
   - Extracted `extractKeys(String text)` → `Map<String, List<Integer>>` as a pure-Java static method (no Eclipse deps)
   - Existing `checkAndMarkDuplicateKeyInString` now calls `extractKeys()` then creates markers from the result
   - Tests: `=`/`:`/space/tab separators, multiple distinct keys, duplicates, triplicates, comments, blanks, escaped separators (`\=`, `\:`), multi-line values, empty input, key-only lines, leading whitespace
   - `replace()`: basic substitution, no-match

   **Total: 51 tests, 0 failures**

### Phase 7: Cleanup and Polish — DONE

1. **Icons** — SKIPPED (no PNG/SVG source files available; 26 GIF icons retained as-is)

   **All GIF icon resources in the project:**

   **Eclipse plugin icons** (`icons/`):
   - `icons/IconByTaroTw.gif` - Main plugin icon
   - `icons/IconByTaroTw_disable.gif` - Disabled version
   - `icons/alphab_sort_co.gif` - Sort action
   - `icons/editPage.gif` - Edit page
   - `icons/outlineMarker.gif` - Outline marker
   - `icons/pe_32.gif` - 32x32 editor icon
   - `icons/previewPage.gif` - Preview page
   - `icons/propedit_marker.gif` - Properties editor marker
   - `icons/psearch_obj.gif` - Search object

   **Standalone app icons** (`src/jp/gr/java_conf/ussiy/app/propedit/resource/`):
   - `resource/ColumnInsertBefore16.gif`
   - `resource/Copy16.gif`
   - `resource/Cut16.gif`
   - `resource/Delete16.gif`
   - `resource/Find16.gif`
   - `resource/FindAgain16.gif`
   - `resource/Information16.gif`
   - `resource/New16.gif`
   - `resource/Open16.gif`
   - `resource/Paste16.gif`
   - `resource/Replace16.gif`
   - `resource/Save16.gif`
   - `resource/Stop16.gif`
   - `resource/Undo16.gif`
   - `resource/editor.gif`
   - `resource/pe_16.gif`
   - `resource/pe_32.gif`

2. **Bundle version** — DONE: Bumped `Bundle-Version` to `7.0.0.qualifier` in `MANIFEST.MF` and `<version>` to `7.0.0-SNAPSHOT` in `pom.xml`
3. **README.md** — DONE: Rewritten with project description, features, prerequisites, build instructions, run commands, project structure, and license reference
4. **GEMINI.md** — DONE: Rewritten with accurate tech stack table, feature list, build/run commands, and key entry points (removed false claims about Eclipse 4/e4, LSP, TM4E)
5. **`.gitignore`** — DONE: Fixed `.settings/` pattern from `*/.settings/**/*` to `.settings/`
6. **Eclipse IDE compatibility** — DONE:
   - Created `.project` (PDE + Java natures) and `.classpath` (PDE required plugins, JUnit 5, FlatLaf, separate test source folder)
   - Removed deprecated `Bundle-RequiredExecutionEnvironment: JavaSE-25` from `MANIFEST.MF` (Eclipse 2024-12 OSGi doesn't know Java 25)
   - Lowered `java.version` to `21` in `pom.xml` and `build.properties` (Eclipse 2024-12 runs on Java 21; code only needs Java 10+ features)
   - Changed `build.properties` `output..` to `target/classes/` (shared with Maven)
   - Fixed surefire regression: added `surefire-junit-platform` provider dependency (Tycho injects JUnit 4, causing JUnit4Provider auto-detection)
7. **UTF-8 encoding fix** — DONE:
   - `ProjectProperties.loadProjectProperties()`: Changed `Properties.load(InputStream)` → `Properties.load(InputStreamReader)` using `IFile.getCharset()` with UTF-8 fallback. Fixes hover/completion mojibake for native UTF-8 `.properties` files.
   - `CheckAndMarkResourceVisitor.visit()`: Changed `ByteArrayOutputStream.toString()` → `toString(Charset)` using `IFile.getCharset()` with UTF-8 fallback. Fixes duplicate key detection for UTF-8 files.
   - Added 2 tests for UTF-8 key parsing. **Total: 53 tests, 0 failures.**

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
