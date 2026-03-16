# Feature Landscape: PropEditorX

**Domain:** Java Localization Tooling
**Researched:** 2024-05-22

## Table Stakes

Features users expect in any localized properties editor.

| Feature | Why Expected | Complexity | Notes |
|---------|--------------|------------|-------|
| **Dual-Mode Editor** | Need to switch between raw text and side-by-side grid view. | High | Requires robust sync between the two editors. |
| **UTF-8 Support** | Standard for modern Java development. | Low | Must handle BOM and non-BOM files. |
| **Grid Column per Locale** | Easy comparison of translations. | Medium | Dynamic columns based on discovered `_locale` files. |
| **Key Filtering/Search** | Find specific keys in large files (1000+ keys). | Medium | Essential for productivity. |

## Differentiators

Features that set PropEditorX apart from standard text editors.

| Feature | Value Proposition | Complexity | Notes |
|---------|-------------------|------------|-------|
| **Missing Key Detection** | Highlighting keys missing in specific locales. | Low | Huge time saver for devs. |
| **Validation Rules** | Checking for missing placeholders (e.g., `{0}`) in translations. | Medium | Prevents runtime errors. |
| **Comment Preservation** | Retain `#` comments when saving from the grid view. | High | Standard `java.util.Properties` strips them. |
| **Refactoring Sync** | Sync key changes across all locale files automatically. | Medium | Prevents manually renaming 10 files. |
| **AI Suggest Translation** | One-click translation for prototyping. | Medium | Integration with external LLM/API. |

## Anti-Features

Features to explicitly NOT build (to keep the scope focused).

| Anti-Feature | Why Avoid | What to Do Instead |
|--------------|-----------|-------------------|
| **Full TMS Functionality** | Overly complex; competes with dedicated web platforms. | Stay focused as an IDE *plugin*. |
| **Runtime Library** | Adding dependencies to the *app* being localized. | Only provide *development-time* tools. |
| **Multi-Format Support (YAML/JSON)** | Dilutes focus on `.properties` which has unique challenges. | Re-evaluate in a later phase if requested. |

## Feature Dependencies

```
Dual-Mode Foundation → Grid View Implementation
Grid View Implementation → Missing Key Detection
Grid View Implementation → Comment Preservation
```

## MVP Recommendation

Prioritize:
1. **Working Dual-Mode Editor:** Fixed rendering, stable switching.
2. **Side-by-Side Grid View:** Ability to see English and one other locale at once.
3. **Missing Key Highlighting:** Clear visual feedback for incomplete translations.

Defer: **AI Translation** and **Advanced Refactoring** to Phase 2/3.

## Sources
- [Eclipse ResourceBundle Editor (Legacy) - Feature list](https://marketplace.eclipse.org/content/resourcebundle-editor)
- [Visual Studio Code - i18n Ally extension features]
