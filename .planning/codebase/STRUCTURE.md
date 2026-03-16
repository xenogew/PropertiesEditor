# Codebase Structure

**Analysis Date:** 2025-03-14

## Directory Layout

```
PropertiesEditor/
├── bundles/                        # Core plugin code
│   └── io.github.xenogew.propedit/  # Main plugin project
│       ├── icons/                  # Visual assets
│       ├── META-INF/               # OSGi bundle manifest
│       ├── schema/                 # Extension point definitions
│       └── src/                    # Java source code
│           ├── io/github/xenogew/propedit/
│           │   ├── bean/           # Simple data objects
│           │   ├── eclipse/        # Plugin-specific logic
│           │   │   └── plugin/
│           │   │       ├── action/ # UI Commands/Handlers
│           │   │       ├── editors/# Editor UI & Model
│           │   │       ├── jdt/    # Java Editor integration
│           │   │       ├── startup/# Initialization
│           │   │       └── util/   # Internal helpers
│           │   └── io/             # Low-level I/O
│           └── test/               # JUnit test suite
├── features/                       # Feature definitions for Eclipse
├── releng/                         # Release Engineering (Update site)
└── target-platform/                # Target platform configuration
```

## Directory Purposes

**bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/:**
- Purpose: Contains the multi-page editor implementation and its supporting model.
- Key files: `PropEditorX.java`, `PropertyBundleModel.java`, `PropertiesGridPage.java`.

**bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/jdt/:**
- Purpose: Implements Eclipse JDT extensions to enable property-aware features in the Java editor.
- Key files: `PropertiesCompletionProposalComputer.java`, `PropertiesHover.java`, `PropertiesHyperlinkDetector.java`.

**bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/startup/:**
- Purpose: Handles plugin lifecycle and workspace monitoring.
- Key files: `Startup.java`.

**bundles/io.github.xenogew.propedit/schema/:**
- Purpose: Defines custom extension points provided by this plugin.
- Key files: `listeners.exsd`, `hyperlinkdetectors.exsd`.

## Key File Locations

**Entry Points:**
- `bundles/io.github.xenogew.propedit/plugin.xml`: Core plugin configuration and extension registration.
- `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropEditorX.java`: Primary editor class.

**Configuration:**
- `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/preference/`: UI for global settings.

**Core Logic:**
- `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/PropertyBundleModel.java`: Model for property bundles.
- `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/util/ProjectProperties.java`: Global property cache.

**Testing:**
- `bundles/io.github.xenogew.propedit/src/test/java/`: Location for JUnit test cases.

## Naming Conventions

**Files:**
- Java: PascalCase (e.g., `PropEditorX.java`).
- Properties: Typically lowercase with underscores for locales (e.g., `messages_ja.properties`).

**Directories:**
- Package names: Lowercase (e.g., `io.github.xenogew.propedit`).

## Where to Add New Code

**New Editor Feature:**
- UI components: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/editors/`
- Command/Action: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/action/`

**New JDT Integration:**
- Feature implementation: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/jdt/`
- Register in `bundles/io.github.xenogew.propedit/plugin.xml`.

**Utilities:**
- Helper methods: `bundles/io.github.xenogew.propedit/src/io/github/xenogew/propedit/eclipse/plugin/util/`

## Special Directories

**.planning/codebase/:**
- Purpose: Analysis and mapping documentation (internal).
- Committed: Yes.

---

*Structure analysis: 2025-03-14*
