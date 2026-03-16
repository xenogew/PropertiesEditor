# Domain Pitfalls: PropEditorX

**Domain:** Java Properties Editor / Localization Tooling
**Researched:** 2024-05-22

## Critical Pitfalls

Mistakes that cause rewrites or major issues.

### Pitfall 1: Character Corruption (Encoding)
**What goes wrong:** User opens a UTF-8 file, but the editor saves it as ISO-8859-1. All non-Latin characters turn into garbage (`?` or `Ã©`).
**Why it happens:** Default `java.util.Properties` and older Eclipse versions defaults.
**Consequences:** Permanent data loss of translations.
**Prevention:** Explicitly detect and store the file's encoding. Use `UTF-8` by default but allow overrides.
**Detection:** Visual markers for non-ASCII characters that don't match the current encoding.

### Pitfall 2: Comment Loss
**What goes wrong:** User edits a file in the Grid view and saves. All developer comments (`# TODO: translate this`) are gone.
**Why it happens:** Most standard property parsers treat comments as "not data" and don't load them into memory.
**Consequences:** Loss of valuable context for developers and translators.
**Prevention:** Use a "Round-Trip" parser (like Apache Commons Configuration) that preserves non-data lines.

## Moderate Pitfalls

### Pitfall 1: Unsynchronized Undo/Redo
**What goes wrong:** User edits in Grid, then switches to Raw and presses Ctrl+Z. The Raw editor undoes a previous change, but the Grid editor is now out of sync with the actual file state.
**Prevention:** Use the Eclipse `CommandStack` and `IUndoContext` to share undo history between both pages of the Multi-Page Editor.

### Pitfall 2: Resource Bundle Resolution Failure
**What goes wrong:** The editor fails to find `messages_fr.properties` because it's in a different folder or has a slightly different naming convention (e.g., `messages-fr.properties`).
**Prevention:** Allow users to manually "Link" files or define a custom naming pattern.

## Minor Pitfalls

### Pitfall 1: Large File Performance
**What goes wrong:** Opening a 5MB properties file (100k+ lines) freezes the UI.
**Prevention:** Use `SWT.VIRTUAL` for tables and background threads for loading/parsing.

## Phase-Specific Warnings

| Phase Topic | Likely Pitfall | Mitigation |
|-------------|---------------|------------|
| Dual-Mode Foundation | Synchronization lag. | Use an event-based model where the Raw editor's IDocument is the "Source of Truth". |
| Validation Suite | False positives for "Missing Keys" in intentionally incomplete locales. | Allow marking specific keys as "Ignored" or "Draft". |
| Advanced UX | Over-cluttering the UI with too many columns. | Implement a "Column Selector" to show/hide specific locales. |

## Sources

- [Common Java I18n Mistakes - Baeldung](https://www.baeldung.com/java-internationalization-mistakes)
- [Stack Overflow - Properties file comment preservation]
- [Eclipse Community Forum - Multi-page editor synchronization patterns]
