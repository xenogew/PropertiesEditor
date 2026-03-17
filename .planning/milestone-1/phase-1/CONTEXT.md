# Phase 1: Grid Performance Optimization (Context)

## 1. Architectural Strategy: "From Scratch" Reconstruction
We are abandoning the widget-per-row approach. The editor will be rebuilt using **Nebula NatTable** as the high-performance, virtualized table scaffold, following the **MVVM (Model-View-ViewModel)** pattern.
- **Pattern**: MVVM. The `PropertyBundleModel` acts as the Model, a new `PropertiesGridViewModel` will manage the state and bridge data to the View, and `PropertiesGridPage` will serve as the View.
- **Library**: Nebula NatTable Core + NatTable e4 Extension.
- **Layer Stack**: DataLayer -> SpanningDataLayer -> SelectionLayer -> ViewportLayer.
- **Features**: Native Virtualization, SpanningDataLayer for rowspan effects, e4 CSS styling, and Flyweight Editing.

## 2. Interaction & Activation
- **Activation**: Single-click on a locale cell immediately enters edit mode.
- **Navigation**: Pressing `Enter` commits the change and moves focus to the same locale in the *next* property row.
- **Initial State**: New locale cells remain empty until user interaction.
- **Key Editing**: Property keys are editable but protected. A 'wrench' icon (Slate-200) must be clicked to trigger a confirmation dialog before the key becomes editable.
- **Cancellation**: Pressing `Esc` reverts the cell to its original state and exits edit mode.

## 3. Visual Density & Layout (Lokalise + Excel Style)
- **Table Structure**:
    - **Column 1 (Key)**: Simulates a "rowspan" equal to the number of detected locales.
    - **Column 2 (Locale)**: A dedicated header column with Slate-400 background and Bold text.
    - **Column 3 (Value)**: The editable translation area.
- **Grid Lines**: Implement subtle gray lines (Excel-style) to guide the user's eye.
- **Rendering Priority**: During scrolling, the "Property Key" must be prioritized in the set-data event to ensure the user never loses their place.
- **Padding**: Horizontal padding `pl-2` (8-10px) for all text within cells.

## 4. Data Synchronization & State
- **Flushing**: Implement a "Debounce" pattern—save the value to the model when the user stops typing for a short duration or when focus is lost.
- **Tab Switching**: If the Grid is dirty, switching to a Source tab MUST trigger a "Save changes?" dialog.
- **Scope**: Focus exclusively on visible data. No "Select All" or background processing of non-rendered cells is required for this phase.

## 5. Code Context & Integration
- **Existing Assets**: Use `PropertiesFileUtil.extractBaseName` for bundle identification.
- **Target environment**: `/home/xenogew/sandbox/test_i18n` (Reference for testing flows).
- **Standards**: Java 25, Virtual Threads for model reloads, strict Clean Code/SOLID.
