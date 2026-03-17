# Validation: Phase 1 - Grid Performance Optimization

## Goal
Verify the successful transition of the multi-locale grid to a high-performance, virtualized Nebula NatTable with a flyweight editor.

## Verification Requirements

### V-1: Infrastructure & Dependencies
- [ ] Project compiles with `org.eclipse.nebula.widgets.nattable.core` and `org.eclipse.nebula.widgets.nattable.extension.e4`.
- [ ] `PropertyBundleModel` implementation correctly feeds data to NatTable layers.

### V-2: Performance & Scalability
- [ ] Smooth scrolling (>60fps) with 1,000+ property keys.
- [ ] Memory usage remains stable (no widget-per-row leaks).
- [ ] Immediate opening of large properties bundles.

### V-3: Layout & Styling
- [ ] "Property Key" column correctly spans N rows (locales).
- [ ] Locale headers have `Slate-400` (#94a3b8) background and Bold font.
- [ ] Grid lines are subtle gray (#e2e8f0).
- [ ] Cell padding follows the `pl-2` (8-10px) horizontal requirement.

### V-4: Interaction & UX
- [ ] Single-click on a locale cell activates the `TextCellEditor`.
- [ ] `Enter` key commits change and moves focus to the next property row.
- [ ] `Esc` key cancels the current edit and reverts value.
- [ ] 'Wrench' icon protection: Key editing requires confirmation.

### V-5: Data Integrity
- [ ] Changes in Grid are debounced before flushing to the model.
- [ ] Switching tabs prompts a "Save changes?" dialog if the grid is dirty.
- [ ] No regression in UTF-8 character preservation.

## Automated Verification Tests
- **Performance Integration Test**: Script to generate a properties bundle with 5,000 keys and verify loading time.
- **Model Unit Tests**: Verify `ISpanningDataProvider` returns correct row/column mapping.
