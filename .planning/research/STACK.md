# Technology Stack: PropEditorX

**Project:** PropertiesEditor / PropEditorX
**Researched:** 2024-05-22

## Recommended Stack

### Core Framework
| Technology | Version | Purpose | Why |
|------------|---------|---------|-----|
| Eclipse RCP (SWT) | 2024-03+ | Plugin Infrastructure | Existing codebase is built on Eclipse bundles; standard for Java IDE plugins. |
| Java (JDK) | 17/21 | Runtime Environment | Modern LTS versions provide better performance, UTF-8 by default, and `Locale.of()`. |

### UI & UX
| Technology | Version | Purpose | Why |
|------------|---------|---------|-----|
| SWT (Standard Widget Toolkit) | Latest | Native UI | Deep integration with Eclipse IDE; high performance. |
| JFace | Latest | Data Binding & Viewers | Simplifies Table and Grid implementations; standard in Eclipse. |

### Supporting Libraries
| Library | Version | Purpose | When to Use |
|---------|---------|---------|-------------|
| ICU4J | Latest | Internationalization | For advanced `MessageFormat` support (plurals, gender). |
| Apache Commons Configuration | 2.x | Properties Parsing | Robust handling of different `.properties` formats and encodings. |

## Alternatives Considered

| Category | Recommended | Alternative | Why Not |
|----------|-------------|-------------|---------|
| UI Framework | Eclipse SWT | JavaFX | SWT has better integration with the existing Eclipse codebase and IDE workspace. |
| Properties Lib | Apache Commons | Standard `java.util.Properties` | Standard `Properties` class loses comments and formatting when saving. |

## Installation

```xml
<!-- In pom.xml (Tycho configuration for Eclipse plugins) -->
<dependency>
  <groupId>org.eclipse.platform</groupId>
  <artifactId>org.eclipse.swt</artifactId>
  <version>3.125.0</version>
</dependency>
```

## Sources

- [Eclipse Platform Documentation](https://help.eclipse.org/latest/index.jsp)
- [Apache Commons Configuration](https://commons.apache.org/proper/commons-configuration/)
- [Existing project pom.xml and bundle structure]
